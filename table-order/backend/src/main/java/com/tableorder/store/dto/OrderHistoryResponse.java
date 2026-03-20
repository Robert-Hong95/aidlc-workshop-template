package com.tableorder.store.dto;

import java.time.LocalDateTime;

public record OrderHistoryResponse(Long id, String orderData, Integer totalAmount,
                                   LocalDateTime orderedAt, LocalDateTime completedAt) {}
