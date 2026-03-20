package com.tableorder.order.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseEmitterService {

    private static final long TIMEOUT = 30_000L;

    private final Map<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long storeId, String clientType, String clientId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        String key = buildKey(storeId, clientType, clientId);
        emitters.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeEmitter(key, emitter));
        emitter.onTimeout(() -> removeEmitter(key, emitter));
        emitter.onError(e -> removeEmitter(key, emitter));
        try { emitter.send(SseEmitter.event().name("connect").data("connected")); } catch (IOException ignored) {}
        return emitter;
    }

    public void publishToStore(Long storeId, Object data) {
        sendToPrefix("store:" + storeId + ":ADMIN:", data);
    }

    public void publishToTable(Long storeId, Long tableId, Object data) {
        String key = buildKey(storeId, "TABLE", String.valueOf(tableId));
        sendToKey(key, data);
    }

    private void sendToPrefix(String prefix, Object data) {
        emitters.forEach((key, list) -> {
            if (key.startsWith(prefix)) {
                list.forEach(emitter -> {
                    try { emitter.send(SseEmitter.event().name("order").data(data)); } catch (IOException e) { list.remove(emitter); }
                });
            }
        });
    }

    private void sendToKey(String key, Object data) {
        CopyOnWriteArrayList<SseEmitter> list = emitters.get(key);
        if (list != null) {
            list.forEach(emitter -> {
                try { emitter.send(SseEmitter.event().name("order").data(data)); } catch (IOException e) { list.remove(emitter); }
            });
        }
    }

    private void removeEmitter(String key, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> list = emitters.get(key);
        if (list != null) list.remove(emitter);
    }

    private String buildKey(Long storeId, String clientType, String clientId) {
        return "store:" + storeId + ":" + clientType + ":" + clientId;
    }
}
