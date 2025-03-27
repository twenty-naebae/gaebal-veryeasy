package com.gaebal_easy.client.hub.infrastructure.config;

import com.gaebal_easy.client.hub.application.dto.HubDirectDto;
import com.gaebal_easy.client.hub.application.dto.HubRouteDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, HubDirectDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, HubDirectDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 객체 직렬화
        return template;
    }

    @Bean
    public RedisTemplate<String, HubRouteDto> redisRouteTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, HubRouteDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 객체 직렬화
        return template;
    }
}
