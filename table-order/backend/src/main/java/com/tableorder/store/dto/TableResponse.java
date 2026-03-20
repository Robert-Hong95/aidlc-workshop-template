package com.tableorder.store.dto;

public record TableResponse(Long id, Long storeId, Integer tableNo, boolean hasActiveSession) {}
