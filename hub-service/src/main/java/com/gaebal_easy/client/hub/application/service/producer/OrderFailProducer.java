package com.gaebal_easy.client.hub.application.service.producer;

import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaOrderFailDto;
import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaStoreCreateAssignDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderFailProducer {
    private static final String TOPIC = "order_fail";

    private final KafkaTemplate<String, KafkaOrderFailDto> kafkaOrderFailDtoKafkaTemplate;

    public void sendOrderFailEvent(UUID orderId, String message) {
        KafkaOrderFailDto event = new KafkaOrderFailDto(orderId, message);
        kafkaOrderFailDtoKafkaTemplate.send(TOPIC, event);
    }
}
