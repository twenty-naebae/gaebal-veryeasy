package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.presentation.adapter.out.HubManagerEventConsumer;
import gaebal_easy.common.global.dto.BaseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventErrorHandler {

    private final HubManagerEventConsumer hubManagerEventConsumer;

    public <T extends BaseMessage> void handleEventError(
            Exception exception,
            T message,
            String errorTopic){

        if(message.getErrorLocation()==null){
            message.setErrorInfo("hub-service", exception.getMessage());
        }
        hubManagerEventConsumer.sendErrorEvent(message, errorTopic);
    }

}
