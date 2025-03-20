package com.gaebal_easy.hub.presentation;

import com.gaebal_easy.hub.application.service.HubManagerService;
import com.gaebal_easy.hub.presentation.dto.HubManagerInfoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubManagerEventListener {

    private final HubManagerService hubManagerService;

    @KafkaListener(topics = "hub-manager-info", groupId = "hub-manager")
    public void handleHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        try {
            log.info("handleHubManagerInfo : {}", hubManagerInfoMessage);
            hubManagerService.createHubManager(hubManagerInfoMessage);
        } catch (Exception e) {
            log.error("handleHubManagerInfo error : {}", e.getMessage());
        }
    }
}
