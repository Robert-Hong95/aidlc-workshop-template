package com.tableorder.auth.dto;

public record AuthTokens(String accessToken, String refreshToken, long expiresIn, String role) {}
