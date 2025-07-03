package com.gabrielAmaro.encurtador_URL.service; 

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class UrlService {

    private final StringRedisTemplate redisTemplate;
    private final SecureRandom random = new SecureRandom();

    public UrlService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String generateShortCode(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public String shortenUrl(String longUrl) { 
        final int CODE_LENGTH = 6;
        String shortCode;
        
        do {
            shortCode = generateShortCode(CODE_LENGTH);
        } while (Boolean.TRUE.equals(redisTemplate.hasKey(shortCode)));

        redisTemplate.opsForValue().set(shortCode, longUrl);
        
        return shortCode;
    }

    
    public String getOriginalUrl(String shortCode) { 
        return redisTemplate.opsForValue().get(shortCode);
    }
}