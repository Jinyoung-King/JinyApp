package com.jiny.jinyapp.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final StringRedisTemplate redisTemplate;

    private static final String TOTAL_KEY = "visits:total";
    private static final String TODAY_KEY_PREFIX = "visits:";

    public void increaseVisitorCount() {
        String todayKey = getTodayKey();

        redisTemplate.opsForValue().increment(todayKey);
        redisTemplate.opsForValue().increment(TOTAL_KEY);
    }

    public void increaseVisitorCount(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String todayKey = getTodayKey();
        String ipKey = "ip:" + todayKey + ":" + ip;

        // 하루 동안 중복 카운트 방지
        Boolean alreadyVisited = redisTemplate.hasKey(ipKey);
        if (alreadyVisited) return;

        redisTemplate.opsForValue().set(ipKey, "1", Duration.ofHours(24));
        redisTemplate.opsForValue().increment(todayKey);
        redisTemplate.opsForValue().increment(TOTAL_KEY);
    }


    public int getTodayCount() {
        String todayKey = getTodayKey();
        String count = redisTemplate.opsForValue().get(todayKey);
        return count == null ? 0 : Integer.parseInt(count);
    }

    public int getTotalCount() {
        String count = redisTemplate.opsForValue().get(TOTAL_KEY);
        return count == null ? 0 : Integer.parseInt(count);
    }

    private String getTodayKey() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return TODAY_KEY_PREFIX + today;
    }
}
