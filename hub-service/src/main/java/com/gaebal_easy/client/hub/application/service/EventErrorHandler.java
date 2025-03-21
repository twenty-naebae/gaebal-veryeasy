package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoMessage;
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

//    public void handleHumbManagerCreateError(Exception exception, HubManagerInfoMessage hubManagerInfoMessage) {
//        log.error("이벤트 처리 중 오류 발생: {}", exception.getMessage(), exception);
//
//        hubManagerInfoMessage.setErrorInfo("hub-service", exception.getMessage());
//
//        // 오류 정보를 포함한 이벤트 생성
//        kafkaTemplate.send("hub-manager-create-error", hubManagerInfoMessage);
//
//        log.info("전송 완료");
//    }

    public <T extends BaseMessage> void handleEventError(
            Exception exception,
            T message,
            String errorTopic){

        if(message.getErrorLocation()!=null){
            message.setErrorInfo("hub-service", exception.getMessage());
        }
        log.error("이벤트 처리 중 오류 발생: {}", exception.getMessage(), exception);
        kafkaTemplate.send(errorTopic, message);

    }

}
