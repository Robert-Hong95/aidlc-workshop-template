package com.tableorder.menu.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(@NotBlank String name) {}
