package com.tableorder.order.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.order.dto.*;
import com.tableorder.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerOrderController {
    private final OrderService orderService;
    public CustomerOrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/stores/{storeId}/tables/{tableId}/orders")
    public ApiResponse<OrderResponse> create(@PathVariable Long storeId, @PathVariable Long tableId, @Valid @RequestBody OrderCreateRequest req) {
        return ApiResponse.ok(orderService.createOrder(storeId, tableId, req));
    }
    @GetMapping("/stores/{storeId}/tables/{tableId}/orders")
    public ApiResponse<List<OrderResponse>> list(@PathVariable Long storeId, @PathVariable Long tableId) {
        return ApiResponse.ok(orderService.getOrdersByTable(storeId, tableId));
    }
    @DeleteMapping("/orders/{orderId}")
    public ApiResponse<Void> cancel(@PathVariable Long orderId) {
        orderService.deleteOrderByCustomer(orderId); return ApiResponse.ok(null);
    }
}
