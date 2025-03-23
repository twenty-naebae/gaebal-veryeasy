package com.gaebal_easy.client.hub.application.service.producer;

import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaStoreCreateAssignDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreCreateAssignProducer {
    private static final String TOPIC = "hub_store_create_assign";

    private final KafkaTemplate<String, KafkaStoreCreateAssignDto> storeCreateAssignKafkaTemplate;

    public void sendHubAssignedEvent(UUID hubId, UUID storeId) {
        System.out.println("이벤트 재발행 됐나?");
        KafkaStoreCreateAssignDto event = new KafkaStoreCreateAssignDto(hubId, storeId);
        storeCreateAssignKafkaTemplate.send(TOPIC, event);
        System.out.println("Kafka 이벤트 발행: " + event);
    }
}
