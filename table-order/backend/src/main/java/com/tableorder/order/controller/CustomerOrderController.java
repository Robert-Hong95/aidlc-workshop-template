package com.tableorder.order.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.order.dto.*;
import com.tableorder.order.service.OrderService;
import com.tableorder.order.service.SseEmitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "Order (Customer)", description = "고객 주문 API")
@RestController
public class CustomerOrderController {

    private final OrderService orderService;
    private final SseEmitterService sseEmitterService;

    public CustomerOrderController(OrderService orderService, SseEmitterService sseEmitterService) {
        this.orderService = orderService;
        this.sseEmitterService = sseEmitterService;
    }

    @Operation(summary = "주문 생성", description = "첫 주문 시 테이블 세션 자동 시작")
    @PostMapping("/api/customer/stores/{storeId}/tables/{tableId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@PathVariable Long storeId, @PathVariable Long tableId,
                                                                   @Valid @RequestBody OrderCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(orderService.createOrder(storeId, tableId, request)));
    }

    @Operation(summary = "현재 세션 주문 조회")
    @GetMapping("/api/customer/sessions/{sessionId}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(@PathVariable Long sessionId) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getOrdersBySession(sessionId)));
    }

    @Operation(summary = "주문 삭제 (고객)", description = "PENDING 상태만 삭제 가능")
    @DeleteMapping("/api/customer/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId, @RequestParam Long tableId) {
        orderService.deleteOrderByCustomer(orderId, tableId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "SSE 구독 (고객)", description = "주문 상태 변경 실시간 수신. 타임아웃 30초, 자동 재연결")
    @GetMapping("/api/customer/sse/subscribe")
    public SseEmitter subscribe(@RequestParam Long storeId, @RequestParam Long tableId) {
        return sseEmitterService.subscribe(storeId, "TABLE", String.valueOf(tableId));
    }
}
