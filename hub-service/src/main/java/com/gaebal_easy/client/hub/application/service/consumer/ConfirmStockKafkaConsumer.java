package com.gaebal_easy.client.hub.application.service.consumer;

import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStokProductDto;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.OrderFailExceiption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmStockKafkaConsumer {

    private final HubProductListRepository hubProductListRepository;
    private final RedissonClient redissonClient;


    @KafkaListener(groupId = "stock", topics = "confirm_stock")
    public void confirmStock(List<Map<String, Object>> productsAsMap){

        try {
            log.info("Confirm stock product: {}", productsAsMap);

            // Map을 DTO로 변환
            List<CheckStokProductDto> products = productsAsMap.stream()
                    .map(map -> {
                        CheckStokProductDto dto = new CheckStokProductDto();
                        dto.setProductId(UUID.fromString((String)map.get("productId")));
                        dto.setQuantity(((Number)map.get("quantity")).longValue());
                        dto.setPrice(((Number)map.get("price")).longValue());
                        return dto;
                    })
                    .collect(Collectors.toList());

            // 비즈니스 로직 처리
            Map<UUID, Long> productQuantities = new HashMap<>();
            ArrayList<RLock> locks = new ArrayList<>();

            for(CheckStokProductDto product : products){
                productQuantities.put(product.getProductId(), product.getQuantity());
            }
            log.info("프로덕트퀀티티 저장");
            // 모든 상품에 대한 락 획득 시도
            try{
                // 상품 ID 기준으로 정렬하여 데드락 방지
                List<UUID> sortedProductIds = new ArrayList<>(productQuantities.keySet());
                Collections.sort(sortedProductIds);

                for(UUID productId : sortedProductIds){
                    RLock lock = redissonClient.getLock("key:"+productId.toString());
                    locks.add(lock);
                    // 락 획득 시도
                    boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);

                    if (!available){
                        log.info("Lock {} 획득 실패", "key:"+productId.toString());
                        throw new IllegalStateException("Lock을 획득할 수 없습니다.");
                    }
                }
                log.info("락 획득");

                for(UUID productId : sortedProductIds){

                    hubProductListRepository.decreseRealStock(productId, productQuantities.get(productId));


                }
                log.info("재고 확정 처리!!!!!!!!!!");
            } catch (InterruptedException e) {
                throw new OrderFailExceiption(Code.ORDER_FAIL_EXCEIPTION);
            }finally {
                // 획득한 모든 락 해제
                for (RLock lock : locks) {
                    lock.unlock();
                }
            }

        } catch (Exception e) {
            log.error("메시지 역직렬화 중 오류 발생: {}", e.getMessage(), e);
        }



    }

}
