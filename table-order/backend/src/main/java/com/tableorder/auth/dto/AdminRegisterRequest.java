package com.tableorder.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminRegisterRequest(
        @NotBlank String storeCode,
        @NotBlank @Size(min = 2) @Pattern(regexp = "^[a-zA-Z0-9]+$") String username,
        @NotBlank @Size(min = 4) String password
) {}
