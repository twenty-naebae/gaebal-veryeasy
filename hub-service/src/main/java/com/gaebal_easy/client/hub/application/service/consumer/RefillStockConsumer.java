package com.gaebal_easy.client.hub.application.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStockResponse;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStokProductDto;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.ReservationDto;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefillStockConsumer {

    private final HubProductListRepository hubProductListRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;

    @KafkaListener(groupId = "refill", topics = "refill_stock")
    public void refillStock(String message){
        log.info("리필 컨슈머 {}", message);
        try {
            CheckStockResponse checkStockResponse = objectMapper.readValue(message, CheckStockResponse.class);
            List<ReservationDto> reservations = checkStockResponse.getReservations();

            Map<UUID, Long> productQuantities = new HashMap<>();
            ArrayList<RLock> locks = new ArrayList<>();
            for(ReservationDto reservation : reservations){
                productQuantities.put(reservation.getProductId(), reservation.getQuantity());
            }

            try{
                List<UUID> sortedProductIds = new ArrayList<>(productQuantities.keySet());
                Collections.sort(sortedProductIds);

                for(UUID productId: sortedProductIds){
                    RLock lock = redissonClient.getLock("key:"+productId.toString());
                    locks.add(lock);
                    // 락 획득 시도
                    boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);

                    if (!available){
                        log.info("Lock {} 획득 실패", "key:"+productId.toString());
                        throw new IllegalStateException("Lock을 획득할 수 없습니다.");
                    }
                }

                // 비즈니스 로직 처리
                ValueOperations<String, String> ops = redisTemplate.opsForValue();
                for(UUID productId : sortedProductIds) {
                    hubProductListRepository.refillProductAmount(productId, 10000L);
                    ops.set("stock:" + productId, "10000");
                }

            }catch (InterruptedException e) {
                throw new OrderFailExceiption(Code.ORDER_FAIL_EXCEIPTION);
            }finally {
                // 획득한 모든 락 해제
                for (RLock lock : locks) {
                    lock.unlock();
                }
            }





        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }









    }

}
