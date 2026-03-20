package com.tableorder.store.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.domain.Store;
import com.tableorder.store.dto.*;
import com.tableorder.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public StoreResponse createStore(StoreCreateRequest request) {
        storeRepository.findByStoreCode(request.storeCode())
                .ifPresent(s -> { throw new BusinessException(ErrorCode.DUPLICATE_STORE_CODE); });
        Store store = storeRepository.save(new Store(request.storeCode(), request.name()));
        return StoreResponse.from(store);
    }

    public List<StoreResponse> getStores() {
        return storeRepository.findAll().stream().map(StoreResponse::from).toList();
    }

    public StoreResponse getStore(Long storeId) {
        return StoreResponse.from(findStore(storeId));
    }

    public StoreResponse updateStore(Long storeId, StoreUpdateRequest request) {
        Store store = findStore(storeId);
        store.updateName(request.name());
        return StoreResponse.from(store);
    }

    public void deleteStore(Long storeId) {
        storeRepository.delete(findStore(storeId));
    }

    private Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
    }
}
