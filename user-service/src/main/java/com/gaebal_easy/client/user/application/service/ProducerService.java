package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String, HubManagerInfoMessage> kafkaTemplate;

    public void sendHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        kafkaTemplate.send("hub-manager-info", hubManagerInfoMessage);
        log.info("전송 완료");
    }
}
