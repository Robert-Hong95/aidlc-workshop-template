package com.tableorder.table.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TableSetupRequest(
        @NotNull Integer tableNo,
        @NotBlank String password
) {}
