package com.gaebal_easy.client.hub.presentation.adapter.out;

import gaebal_easy.common.global.dto.BaseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubManagerEventConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends BaseMessage> void sendErrorEvent(T message, String errorTopic){
        kafkaTemplate.send(errorTopic, message);
        log.info("에러 메세지 발행 : " + errorTopic + " : " + message);
    }
}
