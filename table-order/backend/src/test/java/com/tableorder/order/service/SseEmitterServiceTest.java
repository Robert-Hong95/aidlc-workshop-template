package com.tableorder.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;

class SseEmitterServiceTest {

    private SseEmitterService sseEmitterService;

    @BeforeEach
    void setUp() { sseEmitterService = new SseEmitterService(); }

    @Test
    void subscribe_연결성공() {
        SseEmitter emitter = sseEmitterService.subscribe(1L, "ADMIN", "admin1");
        assertThat(emitter).isNotNull();
    }

    @Test
    void publishToStore_에러없이실행() {
        sseEmitterService.subscribe(1L, "ADMIN", "admin1");
        sseEmitterService.publishToStore(1L, "test data");
        // no exception = success
    }

    @Test
    void publishToTable_에러없이실행() {
        sseEmitterService.subscribe(1L, "TABLE", "1");
        sseEmitterService.publishToTable(1L, 1L, "test data");
        // no exception = success
    }
}
