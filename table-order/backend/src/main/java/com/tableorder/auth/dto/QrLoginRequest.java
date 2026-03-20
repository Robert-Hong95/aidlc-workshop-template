package com.tableorder.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record QrLoginRequest(@NotBlank String qrToken) {}
