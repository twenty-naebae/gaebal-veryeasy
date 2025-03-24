package com.gaebal_easy.delivery.presentation;

import com.gaebal_easy.delivery.application.service.DeliveryUserAssignmentService;
import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/delivery-service/api")
public class DeliveryUserTextContorller {

    private final DeliveryUserAssignmentService deliveryUserAssignmentService;

    @GetMapping("/test")
    public void assignMentDeliveryUser(){
        HubDeliveryUser hubDeliveryUser = deliveryUserAssignmentService.assignHubDeliveryUser();
        StoreDeliveryUser storeDeliveryUser = deliveryUserAssignmentService.assignStoreDeliveryUser(1L);
        log.info("Assigned hub delivery user id: " + hubDeliveryUser.getId());
        log.info("Assigned store delivery user id: " + storeDeliveryUser.getId());
    }
}
