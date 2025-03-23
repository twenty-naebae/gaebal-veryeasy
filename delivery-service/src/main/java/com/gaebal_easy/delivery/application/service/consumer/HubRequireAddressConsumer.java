package com.gaebal_easy.delivery.application.service.consumer;

import com.gaebal_easy.delivery.application.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import org.springframework.kafka.annotation.KafkaListener;

public class HubRequireAddressConsumer {

    @KafkaListener(topics="hub_require_address", groupId = "delivery_group",containerFactory = "requireAddressToHubKafkaListenerContainerFactory")
    public void hubRequireAddress(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto) {
        // 여기에서 받은 값들을 토대로 feignclient 요청을 보낸 후 delivery와 detailDelivery에 저장해줘야 할듯...
    }
}
