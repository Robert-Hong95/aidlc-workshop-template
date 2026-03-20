package com.tableorder.order.dto;

import com.tableorder.order.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long id, Long storeId, Long tableId, Long sessionId,
                            int totalAmount, OrderStatus status, List<OrderItemResponse> items,
                            LocalDateTime createdAt) {}
