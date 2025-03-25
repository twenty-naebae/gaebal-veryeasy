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
public class RefillStockConsumer {
    private final HubRepository hubRepository;
    private final HubProductListRepository hubProductListRepository;
    private final RedissonClient redissonClient;
    private final CacheManager cacheManager;

    @KafkaListener(groupId = "refill", topics = "refill_stock")
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
            for(CheckStokProductDto product : products){
                hubProductListRepository.refillProductAmount(product.getProductId(), 10000L);
            }


        } catch (Exception e) {
            log.error("메시지 역직렬화 중 오류 발생: {}", e.getMessage(), e);
        }



    }

}
