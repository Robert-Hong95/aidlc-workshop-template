package com.tableorder.auth.dto;

import jakarta.validation.constraints.NotNull;

public record QrTokenRequest(@NotNull Long storeId, @NotNull Integer tableNo) {}
