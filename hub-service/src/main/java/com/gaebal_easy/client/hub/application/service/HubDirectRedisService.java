package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.application.dto.HubDirectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class HubDirectRedisService {
    private final RedisTemplate<String, HubDirectDto> redisTemplate;
    private static final String KEY_PREFIX = "hub:direct:";
    public void saveRedis(HubDirectDto hubDirectDto) {
        String key = KEY_PREFIX + hubDirectDto.getDepartName() + ":" + hubDirectDto.getArriveName();
        redisTemplate.opsForValue().set(key, hubDirectDto);
    }

    public HubDirectDto getDirectRedis(String depart, String arrive){
        String key = KEY_PREFIX + depart + ":" + arrive;
        return redisTemplate.opsForValue().get(key);
    }
}
