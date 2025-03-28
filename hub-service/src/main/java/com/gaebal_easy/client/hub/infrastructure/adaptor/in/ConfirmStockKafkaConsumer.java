package com.gaebal_easy.client.hub.infrastructure.adaptor.in;

import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStokProductDto;
import com.gaebal_easy.client.hub.application.service.consumer.ConfirmStockService;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.OrderFailExceiption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmStockKafkaConsumer {

    private final ConfirmStockService confirmStockService;

    @KafkaListener(groupId = "stock", topics = "confirm_stock")
    public void confirmStock(List<Map<String, Object>> productsAsMap){

        try {
            log.info("Confirm stock product: {}", productsAsMap);

            List<CheckStokProductDto> products = productsAsMap.stream()
                    .map(map -> {
                        CheckStokProductDto dto = new CheckStokProductDto();
                        dto.setProductId(UUID.fromString((String)map.get("productId")));
                        dto.setQuantity(((Number)map.get("quantity")).longValue());
                        dto.setPrice(((Number)map.get("price")).longValue());
                        return dto;
                    })
                    .collect(Collectors.toList());

            confirmStockService.confirmStock(products);
        } catch (Exception e) {
            log.error("메시지 역직렬화 중 오류 발생: {}", e.getMessage(), e);
        }
    }

}
