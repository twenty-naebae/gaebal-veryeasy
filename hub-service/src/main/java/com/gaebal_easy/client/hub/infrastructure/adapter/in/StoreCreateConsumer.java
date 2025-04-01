package com.gaebal_easy.client.hub.infrastructure.adapter.in;

import com.gaebal_easy.client.hub.presentation.dto.kafkaConsumerDto.KafkaStoreCreateDto;
import com.gaebal_easy.client.hub.infrastructure.adapter.out.StoreCreateAssignProducer;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreCreateConsumer {

    private final HubRepository hubRepository;
    private final StoreCreateAssignProducer storeCreateAssignProducer;

    @KafkaListener(topics="store_create", groupId = "hub_group",containerFactory = "storeCreateKafkaListenerContainerFactory")
    public void storeCreate(KafkaStoreCreateDto kafkaStoreCreateDto) {
        if (kafkaStoreCreateDto == null || kafkaStoreCreateDto.getId() == null || kafkaStoreCreateDto.getAddress() == null) {
            log.warn("잘못된 Kafka 메시지를 수신했습니다.");
            return;
        }
        String storeAddress = kafkaStoreCreateDto.getAddress().substring(0,2);
        List<Hub> hubList = hubRepository.findAll();
        for(Hub hub : hubList) {
            String address = hub.getHubLocation().getAddress().substring(0,2);
            if(address.equals(storeAddress)){
                UUID hubId = hub.getId();
                storeCreateAssignProducer.sendHubAssignedEvent(hubId,kafkaStoreCreateDto.getId());
                break;
            }
        }
    }
}
