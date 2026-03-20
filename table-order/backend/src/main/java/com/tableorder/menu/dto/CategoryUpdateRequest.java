package com.tableorder.menu.dto;

import jakarta.validation.constraints.NotBlank;
public record CategoryUpdateRequest(@NotBlank String name) {}
