package com.halcyon.computer.helper.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CacheManager {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }

    public <T> Optional<T> fetch(String key, Class<T> targetClass) {
        Optional<Object> valueOptional = Optional.ofNullable(redisTemplate.opsForValue().get(key));

        if (valueOptional.isEmpty()) {
            return Optional.empty();
        }

        var objectMapper = new ObjectMapper();

        T value = objectMapper.convertValue(valueOptional.get(), targetClass);

        return Optional.of(value);
    }
}
