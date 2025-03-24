package com.gaebal_easy.delivery.infrastructure.redis;

import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.DeliveryUserRedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisDeliveryUserUtil {

    private final RedisTemplate<String, String> redisTemplate;
    public static final String HUB_DELIVERY_LIST_KEY = "delivery:hub:list";
    public static final String STORE_DELIVERY_LIST_KEY_PREFIX = "delivery:store:";

    /**
     * 라운드 로빈 방식으로 다음 인덱스를 가져온다. 배송담당자 할당을 위해 사용.
     * - INCR로 index 증가
     * - list size(배송담당자 수) 만큼 % 연산 처리
     * - index 값이 100000 이상이면 index를 reset
     */
    public Long getNextIndex(String listKey, String indexKey) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        Long listSize = listOps.size(listKey);
        if (listSize == null || listSize == 0) {
            throw new DeliveryUserRedisException(Code.DELIVERY_USER_REDIS_EXCEPTION,"해당 Redis 리스트가 비어있습니다: " + listKey);
        }

        Long currentIndex = valueOps.increment(indexKey);
        if (currentIndex == null) {
            throw new DeliveryUserRedisException(Code.DELIVERY_USER_REDIS_EXCEPTION, "인덱스 증가 실패: " + indexKey);
        }

        Long finalIndex = currentIndex % listSize; // list size(배송담당자 수) 만큼 % 연산 처리

        // 100,000 이상이면 순환 유지한 채로 초기화( 해당 로직없으면 계속 증가만함)
        if (currentIndex >= 100_000) {
            valueOps.set(indexKey, String.valueOf(finalIndex));
            log.info(" Redis Index Reset : {} -> {}", indexKey, finalIndex);
        }

        return finalIndex;
    }

    // 배송담당자 리스트에 추가
    public void addToHubDeliveryUserList(Long userId) {
        redisTemplate.opsForList().rightPush(HUB_DELIVERY_LIST_KEY, String.valueOf(userId));
    }

    public void addToStoreDeliveryUserList(Long userId, UUID hubId) {
        redisTemplate.opsForList().rightPush(getStoreListKey(hubId), String.valueOf(userId));
    }

    public void removeHubUserFromQueue(Long userId) {
        redisTemplate.opsForList().remove(HUB_DELIVERY_LIST_KEY, 0, String.valueOf(userId));
        log.info("허브 배송담당자 Redis 큐에서 제거됨: userId={}", userId);
    }

    public void removeStoreUserFromQueue(UUID hubId, Long userId) {
        String listKey = getStoreListKey(hubId);
        redisTemplate.opsForList().remove(listKey, 0, String.valueOf(userId));
        log.info("업체 배송담당자 Redis 큐에서 제거됨: hubId={}, userId={}", hubId, userId);
    }

    private String getStoreListKey(UUID hubId) {
        return STORE_DELIVERY_LIST_KEY_PREFIX + hubId + ":list";
    }

}
