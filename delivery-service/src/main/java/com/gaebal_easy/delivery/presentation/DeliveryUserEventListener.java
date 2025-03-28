package com.gaebal_easy.delivery.presentation;

import com.gaebal_easy.delivery.application.service.DeliveryUserService;
import com.gaebal_easy.delivery.application.service.EventErrorHandler;
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

    private final DeliveryUserService deliveryUserService;
    private final EventErrorHandler eventErrorHandler;
    @KafkaListener(topics = "delivery-user-create", groupId = "gaebal-group")
    public void handleHubManagerInfo(DeliveryUserInfoMessage deliveryUserInfoMessage) {
        try {
            log.info("handleDeliveryUserInfo : {}", deliveryUserInfoMessage);
            deliveryUserService.createDeliveryUser(deliveryUserInfoMessage);
        } catch (Exception e) {
            eventErrorHandler.handleEventError(e, deliveryUserInfoMessage, "delivery-user-create-error");
        }
    }

    @KafkaListener(topics = "delivery-user-delete", groupId = "gaebal-group")
    public void handleDeliveryUserDelete(DeliveryUserDeleteMessage deliveryUserDeleteMessage) {
        try {
            log.info("handleDeliveryUserDelete : {}", deliveryUserDeleteMessage);
            deliveryUserService.deleteDeliveryUser(deliveryUserDeleteMessage);
        } catch (Exception e) {
            eventErrorHandler.handleEventError(e, deliveryUserDeleteMessage, "delivery-user-delete-error");
        }
    }
}
