package com.gaebal_easy.client.hub.application.service.producer;

import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaOrderFailDto;
import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaRequireAddressToHubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRequireAddressProducer {
    private static final String TOPIC = "hub_require_address";

    private final KafkaTemplate<String, KafkaRequireAddressToHubDto> kafkaRequireAddressToHubDtoKafkaTemplate;

    public void sendRequireAddressEvent(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto) {
        kafkaRequireAddressToHubDtoKafkaTemplate.send(TOPIC, kafkaRequireAddressToHubDto);
    }
}
