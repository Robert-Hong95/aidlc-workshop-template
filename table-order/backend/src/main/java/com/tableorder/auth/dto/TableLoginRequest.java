package com.tableorder.auth.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TableLoginRequest(
        @NotBlank String storeCode,
        @NotNull @Min(1) Integer tableNo,
        @NotBlank String password
) {}
