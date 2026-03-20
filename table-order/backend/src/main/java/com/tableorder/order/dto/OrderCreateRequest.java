package com.tableorder.order.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderCreateRequest(@NotEmpty List<OrderItemRequest> items) {}
