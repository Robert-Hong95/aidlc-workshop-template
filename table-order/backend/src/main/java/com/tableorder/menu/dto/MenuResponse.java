package com.tableorder.menu.dto;

public record MenuResponse(Long id, String name, int price, String description, String imageUrl, Long categoryId, int displayOrder) {}
