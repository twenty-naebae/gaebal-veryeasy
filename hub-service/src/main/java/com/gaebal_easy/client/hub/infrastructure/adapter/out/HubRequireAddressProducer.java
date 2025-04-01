package com.gaebal_easy.client.hub.infrastructure.adapter.out;

import com.gaebal_easy.client.hub.presentation.dto.kafkaProducerDto.KafkaRequireAddressToHubDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubRequireAddressProducer {
    private static final String TOPIC = "hub_require_address";

    private final KafkaTemplate<String, KafkaRequireAddressToHubDto> kafkaRequireAddressToHubDtoKafkaTemplate;

    public void sendRequireAddressEvent(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto) {

        kafkaRequireAddressToHubDtoKafkaTemplate.send(TOPIC, kafkaRequireAddressToHubDto);
        log.info("send require address event to kafka");
    }
}
