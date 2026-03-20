package com.tableorder.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuCreateRequest(
        @NotNull Long categoryId,
        @NotBlank String name,
        int price,
        String description,
        String imageUrl) {}
