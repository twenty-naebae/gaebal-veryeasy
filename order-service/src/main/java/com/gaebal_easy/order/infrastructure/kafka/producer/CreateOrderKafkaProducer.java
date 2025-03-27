package com.gaebal_easy.order.infrastructure.kafka.producer;

import com.gaebal_easy.order.application.dto.CreateOrderKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOrderKafkaProducer {

    private final KafkaTemplate kafkaTemplate;

    /**
     * Order -> Store 배송생성 요청
     * @param orderCreateKafkaDto
     */
    public void createDelivery(CreateOrderKafkaDto orderCreateKafkaDto){
        log.info("Order -> Store 배송생성 요청");
        // 업체에 orderId, receiver, supplier 전송
        kafkaTemplate.send("order_create","order", orderCreateKafkaDto);
    }

}
