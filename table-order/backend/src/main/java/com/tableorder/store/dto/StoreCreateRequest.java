package com.tableorder.store.dto;

import jakarta.validation.constraints.NotBlank;

public record StoreCreateRequest(@NotBlank String storeCode, @NotBlank String name) {}
