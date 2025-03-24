package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import com.gaebal_easy.delivery.domain.repository.StoreDeliveryUserRepository;
import com.gaebal_easy.delivery.infrastructure.redis.RedisDeliveryUserUtil;
import com.gaebal_easy.delivery.presentation.dto.DeliveryUserInfoResponse;
import com.gaebal_easy.delivery.presentation.dto.DeliveryUserUpdateRequest;
import gaebal_easy.common.global.exception.CanNotAccessInfoException;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.HubManagerNotFoundException;
import gaebal_easy.common.global.message.DeliveryUserDeleteMessage;
import gaebal_easy.common.global.message.DeliveryUserInfoMessage;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import gaebal_easy.common.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true)
    public Page<DeliveryUserInfoResponse> getDeliveryUsers(String type, String sort, int page, int size) {

        Sort sortType;
        if(sort.equals("asc")) {
            sortType = Sort.by(Sort.Order.asc("createdAt"));
        } else {
            sortType = Sort.by(Sort.Order.desc("createdAt"));
        }
        Pageable pageable = PageRequest.of(page, size, sortType);

        if ("hub".equals(type)) {
            Page<HubDeliveryUser> hubDeliveryUsers = hubDeliveryUserRepository.findAll(pageable);
            Page<DeliveryUserInfoResponse> hubDeliveryUserInfoResponses = hubDeliveryUsers.map(deliveryUser -> DeliveryUserInfoResponse.of(
                    deliveryUser.getId(), deliveryUser.getUserId(), deliveryUser.getName(), deliveryUser.getSlackId(), null));
            return hubDeliveryUserInfoResponses;
        }
        else{
            Page<StoreDeliveryUser> storeDeliveryUsers = storeDeliveryUserRepository.findAll(pageable);
            Page<DeliveryUserInfoResponse> storeDeliveryUserInfoResponses = storeDeliveryUsers.map(deliveryUser -> DeliveryUserInfoResponse.of(
                    deliveryUser.getId(), deliveryUser.getUserId(), deliveryUser.getName(), deliveryUser.getSlackId(), deliveryUser.getHubId()));
            return storeDeliveryUserInfoResponses;
        }
    }

    @Transactional(readOnly = true)
    public DeliveryUserInfoResponse getDeliveryUser(Long userId, String type, CustomUserDetails customUserDetails) {
        if(!customUserDetails.getUserId().equals(userId.toString()) && !customUserDetails.getRole().equals("MASTER")) {
            throw new CanNotAccessInfoException(Code.DELIVERY_CAN_NOT_ACCESS_USER_INFO);
        }
        if("hub".equals(type)){
            HubDeliveryUser hubDeliveryUser = hubDeliveryUserRepository.findByUserId(userId).orElseThrow(() -> new CanNotFindUserException(Code.DELIVERY_USER_NOT_FOUND_EXCEPTION));
            return DeliveryUserInfoResponse.of(hubDeliveryUser.getId(), hubDeliveryUser.getUserId(), hubDeliveryUser.getName(), hubDeliveryUser.getSlackId(), null);
        } else {
            StoreDeliveryUser storeDeliveryUser = storeDeliveryUserRepository.findByUserId(userId).orElseThrow(() -> new CanNotFindUserException(Code.DELIVERY_USER_NOT_FOUND_EXCEPTION));
            return DeliveryUserInfoResponse.of(storeDeliveryUser.getId(), storeDeliveryUser.getUserId(), storeDeliveryUser.getName(), storeDeliveryUser.getSlackId(), storeDeliveryUser.getHubId());
        }
    }

    public void updateDeliveryUser(Long userId, DeliveryUserUpdateRequest deliveryUserUpdateRequest, String type) {
        if("hub".equals(type)){
            HubDeliveryUser hubDeliveryUser = hubDeliveryUserRepository.findByUserId(userId).orElseThrow(() -> new CanNotFindUserException(Code.DELIVERY_USER_NOT_FOUND_EXCEPTION));
            hubDeliveryUserRepository.update(hubDeliveryUser, deliveryUserUpdateRequest.getName(), deliveryUserUpdateRequest.getSlackId());
        } else {
            StoreDeliveryUser storeDeliveryUser = storeDeliveryUserRepository.findByUserId(userId).orElseThrow(() -> new CanNotFindUserException(Code.DELIVERY_USER_NOT_FOUND_EXCEPTION));
            storeDeliveryUserRepository.update(storeDeliveryUser,deliveryUserUpdateRequest.getName(), deliveryUserUpdateRequest.getSlackId(), deliveryUserUpdateRequest.getHubId());
        }
    }
}
