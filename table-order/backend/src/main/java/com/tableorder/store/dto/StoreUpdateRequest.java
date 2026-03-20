package com.tableorder.store.dto;

import jakarta.validation.constraints.NotBlank;

public record StoreUpdateRequest(@NotBlank String name) {}
