package com.gaebal_easy.client.user.presentation;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.application.service.DeliveryUserEventService;
import gaebal_easy.common.global.message.DeliveryUserDeleteMessage;
import gaebal_easy.common.global.message.DeliveryUserInfoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryUserEventListener {
    private final DeliveryUserEventService deliveryUserEventService;

    @KafkaListener(topics = "delivery-user-create-error", groupId = "gaebal-group")
    public void handleDeliveryUserInfo(DeliveryUserInfoMessage deliveryUserInfoMessage) {
        try {
            log.error("에러발생! 롤백 진행: "+ "DeliveryUserInfo : {}", deliveryUserInfoMessage);
            deliveryUserEventService.rollbackDeliveryUserInfo(deliveryUserInfoMessage);
        } catch (Exception e) {
            log.error("handleHubManagerInfo : {} message : {}",deliveryUserInfoMessage, ", rollback실패");
        }
    }
    
    @KafkaListener(topics = "delivery-user-delete-error", groupId = "gaebal-group")
    public void handleDeliveryUserDelete(DeliveryUserDeleteMessage deliveryUserDeleteMessage) {
        try {
            log.error("배송 담당자 삭제 에러발생! 롤백 진행: "+ "handleDeliveryUserDelete : {}", deliveryUserDeleteMessage);
            deliveryUserEventService.rollbackDeliveryUserDelete(deliveryUserDeleteMessage);
        } catch (Exception e) {
            log.error("handleDeliveryUserDelete : {} message : {}",deliveryUserDeleteMessage, ", rollback실패");
        }
    }
}
