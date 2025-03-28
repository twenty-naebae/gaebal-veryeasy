package com.gaebal_easy.client.user.presentation.adapter.in;
import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.application.service.HubManagerEventService;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubManagerEventListener {

    private final HubManagerEventService hubManagerEventService;

    @KafkaListener(topics = "hub-manager-create-error", groupId = "gaebal-group")
    public void handleHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        try {
            log.error("에러발생! 롤백 진행: "+ "handleHubManagerInfo : {}", hubManagerInfoMessage);
            hubManagerEventService.rollbackHubManagerInfo(hubManagerInfoMessage);
        } catch (Exception e) {
            log.error("handleHubManagerInfo : {} message : {}",hubManagerInfoMessage, ", rollback실패");
        }
    }

    @KafkaListener(topics = "hub-manager-delete-error", groupId = "gaebal-group")
    public void handleHubManagerDelete(HubManagerDeleteMessage hubManagerDeleteMessage) {
        try {
            log.error("허브매니저 삭제 에러발생! 롤백 진행: "+ "handleHubManagerDeleteMessage : {}", hubManagerDeleteMessage);
            hubManagerEventService.rollbackHubMangerDelete(hubManagerDeleteMessage);
        } catch (Exception e) {
            log.error("handleHubManagerDelete : {} message : {}",hubManagerDeleteMessage, ", rollback실패");
        }
    }
}
