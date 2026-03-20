package com.tableorder.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminLoginRequest(
        @NotBlank String storeCode,
        @NotBlank String username,
        @NotBlank String password
) {}
