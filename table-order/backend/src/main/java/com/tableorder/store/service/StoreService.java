package com.tableorder.store.service;

import com.tableorder.auth.domain.Admin;
import com.tableorder.auth.repository.AdminRepository;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.domain.Store;
import com.tableorder.store.dto.StoreCreateRequest;
import com.tableorder.store.dto.StoreResponse;
import com.tableorder.store.repository.StoreRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StoreService {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,}$";

    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public StoreService(StoreRepository storeRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.storeRepository = storeRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public StoreResponse createStore(StoreCreateRequest request) {
        if (storeRepository.existsByStoreCode(request.storeCode())) {
            throw new BusinessException(ErrorCode.DUPLICATE_STORE_CODE);
        }
        if (!request.adminPassword().matches(PASSWORD_PATTERN)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        Store store = storeRepository.save(new Store(request.storeCode(), request.name()));
        adminRepository.save(new Admin(store.getId(), request.adminUsername(), passwordEncoder.encode(request.adminPassword())));
        return toResponse(store);
    }

    public StoreResponse getStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        return toResponse(store);
    }

    public List<StoreResponse> getStores() {
        return storeRepository.findAll().stream().map(this::toResponse).toList();
    }

    private StoreResponse toResponse(Store store) {
        return new StoreResponse(store.getId(), store.getStoreCode(), store.getName(), store.getCreatedAt());
    }
}
