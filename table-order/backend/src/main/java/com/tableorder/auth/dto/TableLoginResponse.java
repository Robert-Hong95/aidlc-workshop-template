package com.tableorder.auth.dto;

public record TableLoginResponse(String token, Long storeId, String storeName, Long tableId, Integer tableNo) {}
