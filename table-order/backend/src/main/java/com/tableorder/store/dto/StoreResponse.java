package com.tableorder.store.dto;

import com.tableorder.store.domain.Store;
import java.time.LocalDateTime;

public record StoreResponse(Long id, String storeCode, String name, LocalDateTime createdAt) {
    public static StoreResponse from(Store store) {
        return new StoreResponse(store.getId(), store.getStoreCode(), store.getName(), store.getCreatedAt());
    }
}
