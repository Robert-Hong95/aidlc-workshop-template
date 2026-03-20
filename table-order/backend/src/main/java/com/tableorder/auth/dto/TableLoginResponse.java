package com.tableorder.auth.dto;

public record TableLoginResponse(String accessToken, Long storeId, String storeName, Long tableId, Integer tableNo) {}
