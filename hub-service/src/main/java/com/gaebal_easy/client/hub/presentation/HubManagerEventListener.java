package com.gaebal_easy.client.hub.presentation;

import com.gaebal_easy.client.hub.application.service.EventErrorHandler;
import com.gaebal_easy.client.hub.application.service.HubManagerService;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoMessage;
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
    @KafkaListener(topics = "hub-manager-create", groupId = "hub-manager")
    public void handleHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        try {
            log.info("handleHubManagerInfo : {}", hubManagerInfoMessage);
            hubManagerService.createHubManager(hubManagerInfoMessage);
        } catch (Exception e) {
            eventErrorHandler.handleEventError(e, hubManagerInfoMessage, "hub-manager-create-error");
        }
    }
}
