package com.tableorder.store.dto;

import java.time.LocalDateTime;

public record StoreResponse(Long id, String storeCode, String name, LocalDateTime createdAt) {}
