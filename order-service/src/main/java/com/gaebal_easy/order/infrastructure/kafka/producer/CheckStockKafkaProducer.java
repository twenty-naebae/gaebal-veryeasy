package com.gaebal_easy.order.infrastructure.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.order.application.dto.CheckStockDto;
import com.gaebal_easy.order.application.dto.CreateOrderKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckStockKafkaProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Order -> Hub 상품재고 확인 요청
     * @param stockCheckDto
     */
    public void checkStock(CheckStockDto stockCheckDto){
        log.info("Order -> Hub 상품재고 확인 요청");
        try {
            String event = objectMapper.writeValueAsString(stockCheckDto);
            kafkaTemplate.send("check_stock", "stock", event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
