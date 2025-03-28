package com.gaebal_easy.client.hub.presentation.adapter.in;

import com.gaebal_easy.client.hub.application.service.EventErrorHandler;
import com.gaebal_easy.client.hub.application.service.HubManagerService;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoMessage;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubManagerEventListener {

    private final HubManagerService hubManagerService;
    private final EventErrorHandler eventErrorHandler;
    @KafkaListener(topics = "hub-manager-create", groupId = "gaebal-group")
    public void handleHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        try {
            log.info("handleHubManagerInfo : {}", hubManagerInfoMessage);
            hubManagerService.createHubManager(hubManagerInfoMessage);
        } catch (Exception e) {
            eventErrorHandler.handleEventError(e, hubManagerInfoMessage, "hub-manager-create-error");
        }
    }

    @KafkaListener(topics = "hub-manager-delete", groupId = "geabal-group")
    public void handleHubManagerDelete(HubManagerDeleteMessage hubManagerDeleteMessage) {
        try {
            log.info("handleHubManagerDelete : {}", hubManagerDeleteMessage);
            hubManagerService.deleteHubManager(hubManagerDeleteMessage);
        } catch (Exception e) {
            eventErrorHandler.handleEventError(e, hubManagerDeleteMessage, "hub-manager-delete-error");
        }
    }
}
