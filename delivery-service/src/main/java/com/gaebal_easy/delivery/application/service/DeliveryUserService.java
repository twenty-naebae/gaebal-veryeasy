package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import com.gaebal_easy.delivery.domain.repository.StoreDeliveryUserRepository;
import com.gaebal_easy.delivery.infrastructure.redis.RedisDeliveryUserUtil;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.HubManagerNotFoundException;
import gaebal_easy.common.global.message.DeliveryUserDeleteMessage;
import gaebal_easy.common.global.message.DeliveryUserInfoMessage;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
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
    private final RedisDeliveryUserUtil redisDeliveryUserUtil;

    @Transactional
    public void createDeliveryUser(DeliveryUserInfoMessage deliveryUserInfoMessage) {
        try {
            // 소속이 있으면 StoreDeliveryUser
            if (deliveryUserInfoMessage.getGroup() != null) {
                StoreDeliveryUser storeDeliveryUser = StoreDeliveryUser.of(deliveryUserInfoMessage.getUserId(), deliveryUserInfoMessage.getName(), deliveryUserInfoMessage.getSlackId(), deliveryUserInfoMessage.getGroup());
                storeDeliveryUserRepository.save(storeDeliveryUser);
                redisDeliveryUserUtil.addToStoreDeliveryUserList(storeDeliveryUser.getUserId(), storeDeliveryUser.getHubId());
                log.info("업체 배송 담당자 생성 : " + storeDeliveryUser.getName() + ", " + storeDeliveryUser.getHubId());
            } else {
                HubDeliveryUser hubDeliveryUser = HubDeliveryUser.of(deliveryUserInfoMessage.getUserId(), deliveryUserInfoMessage.getName(), deliveryUserInfoMessage.getSlackId());
                hubDeliveryUserRepository.save(hubDeliveryUser);
                redisDeliveryUserUtil.addToHubDeliveryUserList(hubDeliveryUser.getUserId());
                log.info("허브 배송 담당자 생성 : " + hubDeliveryUser.getName());
            }
        }catch (Exception e) {
            log.error("배송 담당자 생성 실패 : " + e.getMessage());
            throw e;
        }

    }

    @Transactional
    public void deleteDeliveryUser(DeliveryUserDeleteMessage deliveryUserDeleteMessage) {
        storeDeliveryUserRepository.findByUserId(deliveryUserDeleteMessage.getUserId()).ifPresent(storeDeliveryUser -> {
            storeDeliveryUserRepository.delete(storeDeliveryUser, deliveryUserDeleteMessage.getDeletedBy());
            redisDeliveryUserUtil.removeStoreUserFromQueue(storeDeliveryUser.getHubId(),storeDeliveryUser.getUserId());
            log.info("업체 배송 담당자 삭제 : " + storeDeliveryUser.getName() + ", " + storeDeliveryUser.getHubId());
        });

        hubDeliveryUserRepository.findByUserId(deliveryUserDeleteMessage.getUserId()).ifPresent(hubDeliveryUser -> {
            hubDeliveryUserRepository.delete(hubDeliveryUser, deliveryUserDeleteMessage.getDeletedBy());
            redisDeliveryUserUtil.removeHubUserFromQueue(hubDeliveryUser.getUserId());
            log.info("허브 배송 담당자 삭제 : " + hubDeliveryUser.getName());
        });
    }
}
