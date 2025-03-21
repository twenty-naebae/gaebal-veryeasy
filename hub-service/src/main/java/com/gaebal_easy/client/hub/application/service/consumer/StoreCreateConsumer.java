package com.gaebal_easy.client.hub.application.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.KafkaStoreCreateDto;
import com.gaebal_easy.client.hub.application.service.producer.StoreCreateAssignProducer;
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
        log.info("✅ 받은 Kafka 메시지: {}", kafkaStoreCreateDto);
        System.out.println("이벤트 수신 완료!!  주소 :"+kafkaStoreCreateDto.getAddress());
        try {
            System.out.println("여기까지 되나?");
            System.out.flush(); // 강제 출력
            String asd = kafkaStoreCreateDto.getAddress().substring(0,2);
            System.out.println("찾을 주소!!!!!!!!!!!!!!!!!  "+asd);
        } catch (Exception e) {
            log.error("❌ 예외 발생! 이유:", e);
        }
        System.out.println("여기까지 되나?");

        String storeAddress = kafkaStoreCreateDto.getAddress().substring(0,2);
        System.out.println("찾을 주소!!!!!!!!!!!!!!!!!  "+storeAddress);
        log.info("혹시 로그문제인가?");
        List<Hub> hubList = hubRepository.findAll();
        for(Hub hub : hubList) {
            System.out.println("비교값 : "+hub.getHubLocation().getAddress());
            String address = hub.getHubLocation().getAddress().substring(0,2);
            if(address.equals(storeAddress)){
                System.out.println("＃＃＃＃＃＃＃＃＃찾은 값 : " +hub.getHubLocation().getAddress());
                UUID hubId = hub.getId();
                storeCreateAssignProducer.sendHubAssignedEvent(hubId,kafkaStoreCreateDto.getId());
                break;
            }
        }
    }


}
