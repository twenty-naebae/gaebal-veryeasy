package com.gaebal_easy.client.hub.application.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.gaebal_easy.client.hub.application.dto.HubRouteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
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
        String encodedKey = KEY_PREFIX
            + URLEncoder.encode(depart, StandardCharsets.UTF_8)
            + ":"
            + URLEncoder.encode(arrive, StandardCharsets.UTF_8);
        log.info("Encoded!!!!!!!!!!!!!"+encodedKey);
        return redisTemplate.opsForValue().get(encodedKey);
    }
}
