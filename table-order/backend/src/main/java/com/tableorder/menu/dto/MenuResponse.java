package com.tableorder.menu.dto;

import java.time.LocalDateTime;

public record MenuResponse(Long id, Long storeId, Long categoryId, String name, int price,
                           String description, String imageUrl, int displayOrder, LocalDateTime createdAt) {}
