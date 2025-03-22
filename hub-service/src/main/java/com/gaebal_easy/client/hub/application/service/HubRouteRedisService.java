package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.application.dto.HubRouteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubRouteRedisService {
    private final RedisTemplate<String, HubRouteDto> redisTemplate;
    private static final String KEY_PREFIX = "hub:route:";
    public void saveRedis(HubRouteDto hubRouteDto) {
        String key = KEY_PREFIX + hubRouteDto.getDepart() + ":" + hubRouteDto.getArrive();
        redisTemplate.opsForValue().set(key, hubRouteDto);
    }

    public HubRouteDto getRouteRedis(String depart, String arrive){
        String key = KEY_PREFIX + depart + ":" + arrive;
        return redisTemplate.opsForValue().get(key);
    }
}
