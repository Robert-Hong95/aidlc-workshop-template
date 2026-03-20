package com.tableorder.auth.dto;

public record TokenResponse(String accessToken, long expiresIn, String role) {}
