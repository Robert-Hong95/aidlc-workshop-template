package com.tableorder.order.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.order.dto.*;
import com.tableorder.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    private final OrderService orderService;
    public AdminOrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping("/stores/{storeId}/orders")
    public ApiResponse<List<OrderResponse>> activeOrders(@PathVariable Long storeId) {
        return ApiResponse.ok(orderService.getActiveOrders(storeId));
    }
    @PutMapping("/orders/{orderId}/status")
    public ApiResponse<OrderResponse> updateStatus(@PathVariable Long orderId, @RequestBody OrderStatusRequest req) {
        return ApiResponse.ok(orderService.updateOrderStatus(orderId, req));
    }
    @DeleteMapping("/orders/{orderId}")
    public ApiResponse<Void> delete(@PathVariable Long orderId) {
        orderService.deleteOrderByAdmin(orderId); return ApiResponse.ok(null);
    }
}
