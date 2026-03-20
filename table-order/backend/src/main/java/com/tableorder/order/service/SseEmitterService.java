package com.tableorder.order.service;

import com.tableorder.order.dto.SseEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseEmitterService {
    private static final long TIMEOUT = 600_000L;
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long storeId, String clientType, String clientId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        String key = buildKey(storeId, clientType, clientId);
        emitters.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeEmitter(key, emitter));
        emitter.onTimeout(() -> removeEmitter(key, emitter));
        emitter.onError(e -> removeEmitter(key, emitter));
        return emitter;
    }

    public void publishToStore(Long storeId, SseEvent event) {
        sendToPrefix("store:" + storeId + ":ADMIN:", event);
    }

    public void publishToTable(Long storeId, Long tableId, SseEvent event) {
        sendToPrefix("store:" + storeId + ":CUSTOMER:" + tableId, event);
    }

    @Scheduled(fixedRate = 15000)
    public void heartbeat() {
        SseEvent hb = new SseEvent("heartbeat", "ping");
        emitters.forEach((key, list) -> list.forEach(e -> {
            try { e.send(SseEmitter.event().name(hb.type()).data(hb.data())); }
            catch (IOException ex) { removeEmitter(key, e); }
        }));
    }

    private void sendToPrefix(String prefix, SseEvent event) {
        emitters.forEach((key, list) -> {
            if (key.startsWith(prefix)) {
                list.forEach(e -> {
                    try { e.send(SseEmitter.event().name(event.type()).data(event.data())); }
                    catch (IOException ex) { removeEmitter(key, e); }
                });
            }
        });
    }

    private String buildKey(Long storeId, String clientType, String clientId) {
        return "store:" + storeId + ":" + clientType + ":" + clientId;
    }

    private void removeEmitter(String key, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(key);
        if (list != null) { list.remove(emitter); if (list.isEmpty()) emitters.remove(key); }
    }
}
