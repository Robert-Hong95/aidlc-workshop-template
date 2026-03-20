package com.tableorder.table.dto;

import java.time.LocalDateTime;

public record TableResponse(Long id, int tableNo, Long activeSessionId, LocalDateTime createdAt) {}
