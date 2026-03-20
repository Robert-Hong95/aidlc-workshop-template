package com.tableorder.auth.service;

import com.tableorder.auth.domain.Admin;
import com.tableorder.auth.dto.*;
import com.tableorder.auth.repository.AdminRepository;
import com.tableorder.common.config.JwtTokenProvider;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.domain.Store;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;
    private final StoreTableRepository storeTableRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(StoreRepository storeRepository, AdminRepository adminRepository,
                       StoreTableRepository storeTableRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.storeRepository = storeRepository;
        this.adminRepository = adminRepository;
        this.storeTableRepository = storeTableRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AdminLoginResponse loginAdmin(AdminLoginRequest request) {
        Store store = storeRepository.findByStoreCode(request.storeCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));
        Admin admin = adminRepository.findByStoreIdAndUsername(store.getId(), request.username())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.password(), admin.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        String token = jwtTokenProvider.createToken(admin.getId().toString(),
                Map.of("role", "ADMIN", "storeId", store.getId(), "username", admin.getUsername()));
        return new AdminLoginResponse(token, store.getId(), store.getName(), admin.getId(), admin.getUsername());
    }

    public AdminRegisterResponse registerAdmin(AdminRegisterRequest request) {
        Store store = storeRepository.findByStoreCode(request.storeCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        adminRepository.findByStoreIdAndUsername(store.getId(), request.username())
                .ifPresent(a -> { throw new BusinessException(ErrorCode.DUPLICATE_ADMIN); });
        Admin admin = new Admin(store.getId(), request.username(), passwordEncoder.encode(request.password()));
        admin = adminRepository.save(admin);
        return new AdminRegisterResponse(admin.getId(), store.getId(), admin.getUsername());
    }

    public TableLoginResponse loginTable(TableLoginRequest request) {
        Store store = storeRepository.findByStoreCode(request.storeCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));
        StoreTable table = storeTableRepository.findByStoreIdAndTableNo(store.getId(), request.tableNo())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.password(), table.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        String token = jwtTokenProvider.createToken(table.getId().toString(),
                Map.of("role", "TABLE", "storeId", store.getId(), "tableId", table.getId()));
        return new TableLoginResponse(token, store.getId(), store.getName(), table.getId(), table.getTableNo());
    }
}
