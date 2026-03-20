package com.tableorder.menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuCreateRequest(
        @NotBlank String name,
        @NotNull @Min(0) Integer price,
        String description,
        @NotNull Long categoryId,
        String imageUrl
) {}
