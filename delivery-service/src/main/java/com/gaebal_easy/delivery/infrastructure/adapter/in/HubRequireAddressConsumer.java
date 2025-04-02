package com.gaebal_easy.delivery.infrastructure.adapter.in;

import com.gaebal_easy.delivery.application.dto.DeliveryMapper;
import com.gaebal_easy.delivery.application.service.DeliveryService;
import com.gaebal_easy.delivery.infrastructure.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRequireAddressConsumer {

    private final DeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;


    @KafkaListener(topics = "hub_require_address", groupId = "delivery_group",
            containerFactory = "requireAddressToHubKafkaListenerContainerFactory")
    public void hubRequireAddress(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto) {
        deliveryService.saveDelivery(deliveryMapper.toDTO(kafkaRequireAddressToHubDto));
    }
}
