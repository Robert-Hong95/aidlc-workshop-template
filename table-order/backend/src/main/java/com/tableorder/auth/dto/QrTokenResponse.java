package com.tableorder.auth.dto;

public record QrTokenResponse(String qrToken, String qrUrl, long expiresIn) {}
