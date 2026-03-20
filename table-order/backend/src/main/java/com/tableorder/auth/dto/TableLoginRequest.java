package com.tableorder.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TableLoginRequest(
    @NotBlank String storeCode,
    @NotNull Integer tableNo,
    @NotBlank String password
) {}
