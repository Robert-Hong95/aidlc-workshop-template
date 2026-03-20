package com.tableorder.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TableSetupRequest(
        @NotNull @Min(1) Integer tableNo,
        @NotBlank @Size(min = 4) String password
) {}
