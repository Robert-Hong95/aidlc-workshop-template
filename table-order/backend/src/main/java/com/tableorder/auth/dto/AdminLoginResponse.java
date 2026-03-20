package com.tableorder.auth.dto;

public record AdminLoginResponse(String accessToken, Long storeId, String storeName, Long adminId, String username) {}
