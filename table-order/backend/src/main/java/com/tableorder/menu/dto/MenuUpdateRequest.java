package com.tableorder.menu.dto;

public record MenuUpdateRequest(String name, Integer price, String description, Long categoryId, String imageUrl) {}
