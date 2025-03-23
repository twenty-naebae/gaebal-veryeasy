package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import com.gaebal_easy.delivery.domain.repository.StoreDeliveryUserRepository;
import com.gaebal_easy.delivery.infrastructure.persistence.DeliverySequenceManager;
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
    private final DeliverySequenceManager sequenceManager;

    @Transactional
    public void createDeliveryUser(DeliveryUserInfoMessage deliveryUserInfoMessage) {
        try {
            // 소속이 있으면 StoreDeliveryUser
            if (deliveryUserInfoMessage.getGroup() != null) {
                int nextOrder = sequenceManager.getNextStoreDeliveryOrder();
                StoreDeliveryUser storeDeliveryUser = StoreDeliveryUser.of(deliveryUserInfoMessage.getUserId(), deliveryUserInfoMessage.getName(), deliveryUserInfoMessage.getSlackId(), deliveryUserInfoMessage.getGroup(), nextOrder);
                storeDeliveryUserRepository.save(storeDeliveryUser);
                log.info("업체 배송 담당자 생성 : " + storeDeliveryUser.getName() + ", " + storeDeliveryUser.getHubId());
            } else {
                int nextOrder = sequenceManager.getNextHubDeliveryOrder();
                HubDeliveryUser hubDeliveryUser = HubDeliveryUser.of(deliveryUserInfoMessage.getUserId(), deliveryUserInfoMessage.getName(), deliveryUserInfoMessage.getSlackId(), nextOrder);
                hubDeliveryUserRepository.save(hubDeliveryUser);
                log.info("허브 배송 담당자 생성 : " + hubDeliveryUser.getName());
            }
        }catch (Exception e) {
            log.error("배송 담당자 생성 실패 : " + e.getMessage());
            throw e;
        }

    }
}
