package com.gaebal_easy.client.hub.application.service;

import gaebal_easy.common.global.dto.BaseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventErrorHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends BaseMessage> void handleEventError(
            Exception exception,
            T message,
            String errorTopic){

        if(message.getErrorLocation()==null){
            message.setErrorInfo("hub-service", exception.getMessage());
        }
        log.error("이벤트 처리 중 오류 발생: {}", exception.getMessage(), exception);
        kafkaTemplate.send(errorTopic, message);

    }

}
