package com.tableorder.auth.service;

import com.tableorder.auth.domain.Admin;
import com.tableorder.auth.dto.AuthTokens;
import com.tableorder.auth.dto.QrTokenResponse;
import com.tableorder.auth.dto.TokenResponse;
import com.tableorder.auth.repository.AdminRepository;
import com.tableorder.common.config.JwtTokenProvider;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.domain.Store;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AdminRepository adminRepository;
    private final StoreRepository storeRepository;
    private final StoreTableRepository storeTableRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AdminRepository adminRepository, StoreRepository storeRepository,
                       StoreTableRepository storeTableRepository, JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.storeRepository = storeRepository;
        this.storeTableRepository = storeTableRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthTokens loginAdmin(String storeCode, String username, String password) {
        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        Admin admin = adminRepository.findByStoreIdAndUsername(store.getId(), username)
                .orElseThrow(() -> {
                    log.warn("Login failed: username={}, storeCode={}, reason=USER_NOT_FOUND", username, storeCode);
                    return new BusinessException(ErrorCode.INVALID_CREDENTIALS);
                });

        if (admin.isLocked()) {
            log.warn("Login failed: username={}, storeCode={}, reason=ACCOUNT_LOCKED", username, storeCode);
            throw new BusinessException(ErrorCode.LOGIN_ATTEMPTS_EXCEEDED);
        }

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            admin.incrementLoginAttempts();
            adminRepository.save(admin);
            log.warn("Login failed: username={}, storeCode={}, reason=WRONG_PASSWORD, attempts={}",
                    username, storeCode, admin.getLoginAttempts());
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        admin.resetLoginAttempts();
        adminRepository.save(admin);

        Map<String, Object> claims = Map.of(
                "storeId", store.getId(), "adminId", admin.getId(),
                "username", admin.getUsername(), "storeName", store.getName(), "role", "ADMIN");

        return createAuthTokens(String.valueOf(admin.getId()), claims, "ADMIN");
    }

    public AuthTokens loginTable(String storeCode, int tableNo, String password) {
        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        StoreTable table = storeTableRepository.findByStoreIdAndTableNo(store.getId(), tableNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.TABLE_NOT_FOUND));

        if (!passwordEncoder.matches(password, table.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        return createTableAuthTokens(store, table);
    }

    public AuthTokens loginByQrToken(String qrToken) {
        if (!jwtTokenProvider.validateToken(qrToken)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        Claims claims = jwtTokenProvider.parseToken(qrToken);
        if (!"QR".equals(claims.get("type", String.class))) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        String storeCode = claims.get("storeCode", String.class);
        Integer tableNo = claims.get("tableNo", Integer.class);

        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        StoreTable table = storeTableRepository.findByStoreIdAndTableNo(store.getId(), tableNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        return createTableAuthTokens(store, table);
    }

    public QrTokenResponse generateQrToken(Long storeId, int tableNo) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        storeTableRepository.findByStoreIdAndTableNo(storeId, tableNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.TABLE_NOT_FOUND));

        Map<String, Object> claims = Map.of("storeCode", store.getStoreCode(), "tableNo", tableNo);
        String qrToken = jwtTokenProvider.createQrToken("qr", claims);
        String qrUrl = "/qr?token=" + qrToken;

        return new QrTokenResponse(qrToken, qrUrl, jwtTokenProvider.getQrExpiration());
    }

    public AuthTokens refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        Claims claims = jwtTokenProvider.parseToken(refreshToken);
        if (!"REFRESH".equals(claims.get("tokenType", String.class))) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        String subject = claims.getSubject();
        String role = claims.get("role", String.class);

        Map<String, Object> newClaims = new HashMap<>();
        claims.forEach((k, v) -> {
            if (!"tokenType".equals(k) && !"iat".equals(k) && !"exp".equals(k) && !"sub".equals(k)) {
                newClaims.put(k, v);
            }
        });

        return createAuthTokens(subject, newClaims, role);
    }

    private AuthTokens createAuthTokens(String subject, Map<String, Object> claims, String role) {
        String accessToken = jwtTokenProvider.createAccessToken(subject, claims);
        String refreshToken = jwtTokenProvider.createRefreshToken(subject, claims);
        return new AuthTokens(accessToken, refreshToken, jwtTokenProvider.getAccessExpiration(), role);
    }

    private AuthTokens createTableAuthTokens(Store store, StoreTable table) {
        Map<String, Object> claims = Map.of(
                "storeId", store.getId(), "tableId", table.getId(),
                "tableNo", table.getTableNo(), "storeName", store.getName(), "role", "TABLE");
        return createAuthTokens(String.valueOf(table.getId()), claims, "TABLE");
    }
}
