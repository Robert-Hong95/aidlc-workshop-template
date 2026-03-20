package com.tableorder.order.service;

import com.tableorder.order.dto.SseEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.*;

class SseEmitterServiceTest {

    private final SseEmitterService service = new SseEmitterService();

    @Test @DisplayName("TC-SSE-001: subscribe 성공")
    void subscribe_success() {
        SseEmitter emitter = service.subscribe(1L, "ADMIN", "admin-1");
        assertThat(emitter).isNotNull();
    }

    @Test @DisplayName("TC-SSE-002: publishToStore 성공")
    void publishToStore_success() {
        service.subscribe(1L, "ADMIN", "admin-1");
        assertThatCode(() -> service.publishToStore(1L, new SseEvent("NEW_ORDER", "data")))
                .doesNotThrowAnyException();
    }
}
