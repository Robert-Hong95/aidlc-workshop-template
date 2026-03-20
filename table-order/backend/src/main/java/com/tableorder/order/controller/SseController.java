package com.tableorder.order.controller;

import com.tableorder.order.service.SseEmitterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
public class SseController {
    private final SseEmitterService sseEmitterService;
    public SseController(SseEmitterService sseEmitterService) { this.sseEmitterService = sseEmitterService; }

    @GetMapping(value = "/api/admin/stores/{storeId}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter adminSse(@PathVariable Long storeId) {
        return sseEmitterService.subscribe(storeId, "ADMIN", UUID.randomUUID().toString());
    }

    @GetMapping(value = "/api/customer/stores/{storeId}/tables/{tableId}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter customerSse(@PathVariable Long storeId, @PathVariable Long tableId) {
        return sseEmitterService.subscribe(storeId, "CUSTOMER", tableId.toString());
    }
}
