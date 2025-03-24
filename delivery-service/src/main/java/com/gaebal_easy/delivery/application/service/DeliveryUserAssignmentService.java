package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import com.gaebal_easy.delivery.domain.repository.StoreDeliveryUserRepository;
import com.gaebal_easy.delivery.infrastructure.redis.RedisDeliveryUserUtil;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.DeliveryUserRedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryUserAssignmentService {

    private final RedisDeliveryUserUtil redisDeliveryUserUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final HubDeliveryUserRepository hubDeliveryUserRepository;
    private final StoreDeliveryUserRepository storeDeliveryUserRepository;

    // 허브 배송담당자 할당
    public HubDeliveryUser assignHubDeliveryUser() {
        String listKey = "delivery:hub:list";
        String indexKey = "delivery:hub:index";

        Long index = redisDeliveryUserUtil.getNextIndex(listKey, indexKey);
        String userIdStr = redisTemplate.opsForList().index(listKey, index);
        if (userIdStr == null) {
            throw new DeliveryUserRedisException(Code.DELIVERY_USER_REDIS_EXCEPTION, "허브 배송담당자 리스트에서 인덱스 값을 가져오지 못했습니다.");
        }

        Long userId = Long.valueOf(userIdStr);
        return hubDeliveryUserRepository.findByUserId(userId).orElseThrow(() -> new CanNotFindUserException(Code.DELIVERY_USER_NOT_FOUND_EXCEPTION));
    }

    // 업체 배송담당자 할당
    public StoreDeliveryUser assignStoreDeliveryUser(Long hubId) {
        String listKey = "delivery:store:" + hubId + ":list";
        String indexKey = "delivery:store:" + hubId + ":index";

        Long index = redisDeliveryUserUtil.getNextIndex(listKey, indexKey);
        String userIdStr = redisTemplate.opsForList().index(listKey, index);
        if (userIdStr == null) {
            throw new DeliveryUserRedisException(Code.DELIVERY_USER_REDIS_EXCEPTION, "업체 배송담당자 리스트에서 유효한 인덱스 값을 가져오지 못했습니다.");
        }

        Long userId = Long.valueOf(userIdStr);
        return storeDeliveryUserRepository.findByUserId(userId)
                .orElseThrow(() -> new CanNotFindUserException(Code.DELIVERY_USER_NOT_FOUND_EXCEPTION));
    }
}
