package com.tableorder.order.dto;

import com.tableorder.order.domain.OrderStatus;

public record OrderStatusRequest(OrderStatus status) {}
