package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import com.gaebal_easy.delivery.domain.repository.StoreDeliveryUserRepository;
import gaebal_easy.common.global.message.DeliveryUserInfoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryUserService {

    private final HubDeliveryUserRepository hubDeliveryUserRepository;
    private final StoreDeliveryUserRepository storeDeliveryUserRepository;

    @Transactional
    public void createDeliveryUser(DeliveryUserInfoMessage deliveryUserInfoMessage) {
        // 소속이 있으면 StoreDeliveryUser
        if(deliveryUserInfoMessage.getGroup() != null){
            StoreDeliveryUser storeDeliveryUser = StoreDeliveryUser.of(deliveryUserInfoMessage.getUserId(), deliveryUserInfoMessage.getName(), deliveryUserInfoMessage.getSlackId(),deliveryUserInfoMessage.getGroup());
            storeDeliveryUserRepository.save(storeDeliveryUser);
            log.info("업체 배송 담당자 생성 : " + storeDeliveryUser.getName() + ", " + storeDeliveryUser.getHubId());
        }else{
            HubDeliveryUser hubDeliveryUser = HubDeliveryUser.of(deliveryUserInfoMessage.getUserId(), deliveryUserInfoMessage.getName(), deliveryUserInfoMessage.getSlackId());
            hubDeliveryUserRepository.save(hubDeliveryUser);
            log.info("허브 배송 담당자 생성 : " + hubDeliveryUser.getName());
        }

    }
}
