package com.tableorder.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MS = 60_000;

    private final ConcurrentHashMap<String, Deque<Long>> requestLog = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = requestLog.computeIfAbsent(ip, k -> new ConcurrentLinkedDeque<>());

        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > WINDOW_MS) {
            timestamps.pollFirst();
        }

        if (timestamps.size() >= MAX_REQUESTS) {
            response.setStatus(429);
            return false;
        }
        timestamps.addLast(now);
        return true;
    }
}
