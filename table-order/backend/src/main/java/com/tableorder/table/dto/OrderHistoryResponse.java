package com.tableorder.table.dto;

import java.time.LocalDateTime;

public record OrderHistoryResponse(Long id, Long tableId, int totalAmount, String orderData, LocalDateTime orderedAt, LocalDateTime completedAt) {}
