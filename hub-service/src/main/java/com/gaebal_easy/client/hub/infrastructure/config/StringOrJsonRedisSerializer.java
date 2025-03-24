package com.gaebal_easy.client.hub.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class StringOrJsonRedisSerializer implements RedisSerializer<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public byte[] serialize(Object value) throws SerializationException {
        try {
            if (value == null) {
                return new byte[0];
            }
            if (value instanceof String) {
                return ((String) value).getBytes();
            }
            return objectMapper.writeValueAsBytes(value); // JSON 변환
        } catch (Exception e) {
            throw new SerializationException("직렬화 실패", e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            String stringValue = new String(bytes);

            // JSON 형태인지 확인 후 변환
            if (stringValue.startsWith("{") || stringValue.startsWith("[")) {
                return objectMapper.readValue(stringValue, Object.class);
            }
            return stringValue; // 일반 문자열이면 그대로 반환
        } catch (Exception e) {
            throw new SerializationException("역직렬화 실패", e);
        }
    }
}
