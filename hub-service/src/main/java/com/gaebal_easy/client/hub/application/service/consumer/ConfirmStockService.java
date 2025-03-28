package com.gaebal_easy.client.hub.application.service.consumer;

import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStokProductDto;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.OrderFailExceiption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmStockService {

    private final HubProductListRepository hubProductListRepository;
    private final RedissonClient redissonClient;


    public void confirmStock(List<CheckStokProductDto> products){

            Map<UUID, Long> productQuantities = new HashMap<>();
            for(CheckStokProductDto product : products){
                productQuantities.put(product.getProductId(), product.getQuantity());
            }

            ArrayList<RLock> locks = new ArrayList<>();
            try{
                acquiresLock(productQuantities, locks);

                for(UUID productId : productQuantities.keySet()){
                    hubProductListRepository.decreseRealStock(productId, productQuantities.get(productId));
                }

            } catch (InterruptedException e) {
                throw new OrderFailExceiption(Code.ORDER_FAIL_EXCEIPTION);
            }finally {
                for (RLock lock : locks) {
                    lock.unlock();
                }
            }

    }

    private List<UUID> acquiresLock(Map<UUID, Long> productQuantities, ArrayList<RLock> locks) throws InterruptedException {
        List<UUID> sortedProductIds = new ArrayList<>(productQuantities.keySet());
        Collections.sort(sortedProductIds);

        for(UUID productId : sortedProductIds){
            RLock lock = redissonClient.getLock("key:"+productId.toString());
            locks.add(lock);

            boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);

            if (!available){
                log.info("Lock {} 획득 실패", "key:"+productId.toString());
                throw new IllegalStateException("Lock을 획득할 수 없습니다.");
            }
        }
        return sortedProductIds;
    }

}
