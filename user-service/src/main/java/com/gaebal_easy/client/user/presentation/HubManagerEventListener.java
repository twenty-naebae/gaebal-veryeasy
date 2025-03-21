package com.gaebal_easy.client.user.presentation;
import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.application.service.HubManagerEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubManagerEventListener {

    private final HubManagerEventService hubManagerEventService;

    @KafkaListener(topics = "hub-manager-create-error", groupId = "hub-manager")
    public void handleHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        try {
            log.info("handleHubManagerInfo : {}", hubManagerInfoMessage);
            hubManagerEventService.rollbackHubManagerInfo(hubManagerInfoMessage);
        } catch (Exception e) {
            log.error("handleHubManagerInfo : {}", hubManagerInfoMessage, ", rollback실패");
        }
    }
}
