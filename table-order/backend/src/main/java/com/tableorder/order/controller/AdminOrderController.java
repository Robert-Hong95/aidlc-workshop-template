package com.tableorder.order.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.order.dto.*;
import com.tableorder.order.service.OrderService;
import com.tableorder.order.service.SseEmitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "Order (Admin)", description = "관리자 주문 관리 API")
@RestController
public class AdminOrderController {

    private final OrderService orderService;
    private final SseEmitterService sseEmitterService;

    public AdminOrderController(OrderService orderService, SseEmitterService sseEmitterService) {
        this.orderService = orderService;
        this.sseEmitterService = sseEmitterService;
    }

    @Operation(summary = "활성 주문 조회", description = "COMPLETED 제외한 주문 목록")
    @GetMapping("/api/admin/stores/{storeId}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getActiveOrders(@PathVariable Long storeId) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getActiveOrdersByStore(storeId)));
    }

    @Operation(summary = "주문 상태 변경", description = "PENDING→CONFIRMED→PREPARING→COMPLETED 순서만 가능")
    @PutMapping("/api/admin/orders/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(@PathVariable Long orderId, @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.updateOrderStatus(orderId, request.status())));
    }

    @Operation(summary = "주문 삭제 (관리자)", description = "모든 상태의 주문 삭제 가능")
    @DeleteMapping("/api/admin/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "SSE 구독 (관리자)", description = "신규 주문/삭제 실시간 수신. 타임아웃 30초, 자동 재연결")
    @GetMapping("/api/admin/sse/subscribe")
    public SseEmitter subscribe(@RequestParam Long storeId, @RequestParam String adminId) {
        return sseEmitterService.subscribe(storeId, "ADMIN", adminId);
    }
}
