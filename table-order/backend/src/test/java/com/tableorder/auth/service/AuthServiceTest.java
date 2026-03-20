package com.tableorder.auth.service;

import com.tableorder.auth.domain.Admin;
import com.tableorder.auth.dto.AuthTokens;
import com.tableorder.auth.dto.QrTokenResponse;
import com.tableorder.auth.repository.AdminRepository;
import com.tableorder.common.config.JwtTokenProvider;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.domain.Store;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AdminRepository adminRepository;
    @Mock private StoreRepository storeRepository;
    @Mock private StoreTableRepository storeTableRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private AuthService authService;

    private Store store;
    private Admin admin;
    private StoreTable storeTable;

    @BeforeEach
    void setUp() throws Exception {
        store = new Store("STORE001", "테스트매장");
        setId(store, 1L);
        admin = new Admin(1L, "admin", "encodedPassword");
        setId(admin, 1L);
        storeTable = new StoreTable(1L, 1, "encodedTablePw");
        setId(storeTable, 1L);
    }

    private void setId(Object entity, Long id) throws Exception {
        var field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    private void mockTokenCreation() {
        given(jwtTokenProvider.createAccessToken(any(), any())).willReturn("access-token");
        given(jwtTokenProvider.createRefreshToken(any(), any())).willReturn("refresh-token");
        given(jwtTokenProvider.getAccessExpiration()).willReturn(3600000L);
    }

    @Nested
    @DisplayName("loginAdmin")
    class LoginAdmin {

        @Test
        @DisplayName("TC-AUTH-006: 정상 로그인 성공")
        void success() {
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(adminRepository.findByStoreIdAndUsername(any(), eq("admin"))).willReturn(Optional.of(admin));
            given(passwordEncoder.matches("password", "encodedPassword")).willReturn(true);
            mockTokenCreation();

            AuthTokens result = authService.loginAdmin("STORE001", "admin", "password");

            assertThat(result.accessToken()).isEqualTo("access-token");
            assertThat(result.refreshToken()).isEqualTo("refresh-token");
            assertThat(result.role()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("TC-AUTH-007: 매장 코드 없음 → STORE_NOT_FOUND")
        void storeNotFound() {
            given(storeRepository.findByStoreCode("INVALID")).willReturn(Optional.empty());

            assertThatThrownBy(() -> authService.loginAdmin("INVALID", "admin", "password"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND));
        }

        @Test
        @DisplayName("TC-AUTH-008: 사용자명 없음 → INVALID_CREDENTIALS")
        void usernameNotFound() {
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(adminRepository.findByStoreIdAndUsername(any(), eq("unknown"))).willReturn(Optional.empty());

            assertThatThrownBy(() -> authService.loginAdmin("STORE001", "unknown", "password"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS));
        }

        @Test
        @DisplayName("TC-AUTH-009: 비밀번호 불일치 → INVALID_CREDENTIALS + 시도 횟수 증가")
        void wrongPassword() {
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(adminRepository.findByStoreIdAndUsername(any(), eq("admin"))).willReturn(Optional.of(admin));
            given(passwordEncoder.matches("wrong", "encodedPassword")).willReturn(false);

            assertThatThrownBy(() -> authService.loginAdmin("STORE001", "admin", "wrong"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS));

            assertThat(admin.getLoginAttempts()).isEqualTo(1);
            verify(adminRepository).save(admin);
        }

        @Test
        @DisplayName("TC-AUTH-010: 계정 잠금 상태 → LOGIN_ATTEMPTS_EXCEEDED")
        void accountLocked() {
            admin.lock(Duration.ofMinutes(15));
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(adminRepository.findByStoreIdAndUsername(any(), eq("admin"))).willReturn(Optional.of(admin));

            assertThatThrownBy(() -> authService.loginAdmin("STORE001", "admin", "password"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.LOGIN_ATTEMPTS_EXCEEDED));
        }

        @Test
        @DisplayName("TC-AUTH-011: 5회 실패 시 계정 잠금")
        void lockAfterFiveFailures() {
            for (int i = 0; i < 4; i++) admin.incrementLoginAttempts();
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(adminRepository.findByStoreIdAndUsername(any(), eq("admin"))).willReturn(Optional.of(admin));
            given(passwordEncoder.matches("wrong", "encodedPassword")).willReturn(false);

            assertThatThrownBy(() -> authService.loginAdmin("STORE001", "admin", "wrong"))
                    .isInstanceOf(BusinessException.class);

            assertThat(admin.getLoginAttempts()).isEqualTo(5);
            assertThat(admin.isLocked()).isTrue();
        }

        @Test
        @DisplayName("TC-AUTH-012: 로그인 성공 시 시도 횟수 리셋")
        void resetAttemptsOnSuccess() {
            for (int i = 0; i < 3; i++) admin.incrementLoginAttempts();
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(adminRepository.findByStoreIdAndUsername(any(), eq("admin"))).willReturn(Optional.of(admin));
            given(passwordEncoder.matches("password", "encodedPassword")).willReturn(true);
            mockTokenCreation();

            authService.loginAdmin("STORE001", "admin", "password");

            assertThat(admin.getLoginAttempts()).isZero();
        }
    }

    @Nested
    @DisplayName("loginTable")
    class LoginTable {

        @Test
        @DisplayName("TC-AUTH-013: 테이블 인증 성공")
        void success() {
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(storeTableRepository.findByStoreIdAndTableNo(any(), eq(1))).willReturn(Optional.of(storeTable));
            given(passwordEncoder.matches("password", "encodedTablePw")).willReturn(true);
            mockTokenCreation();

            AuthTokens result = authService.loginTable("STORE001", 1, "password");

            assertThat(result.accessToken()).isEqualTo("access-token");
            assertThat(result.role()).isEqualTo("TABLE");
        }

        @Test
        @DisplayName("TC-AUTH-014: 테이블 없음 → TABLE_NOT_FOUND")
        void tableNotFound() {
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(storeTableRepository.findByStoreIdAndTableNo(any(), eq(99))).willReturn(Optional.empty());

            assertThatThrownBy(() -> authService.loginTable("STORE001", 99, "password"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.TABLE_NOT_FOUND));
        }

        @Test
        @DisplayName("TC-AUTH-015: 테이블 비밀번호 불일치 → INVALID_CREDENTIALS")
        void wrongPassword() {
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(storeTableRepository.findByStoreIdAndTableNo(any(), eq(1))).willReturn(Optional.of(storeTable));
            given(passwordEncoder.matches("wrong", "encodedTablePw")).willReturn(false);

            assertThatThrownBy(() -> authService.loginTable("STORE001", 1, "wrong"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS));
        }
    }

    @Nested
    @DisplayName("loginByQrToken")
    class LoginByQrToken {

        @Test
        @DisplayName("TC-AUTH-016: QR 토큰 인증 성공")
        void success() {
            given(jwtTokenProvider.validateToken("qr-jwt")).willReturn(true);
            var claims = io.jsonwebtoken.Jwts.claims().subject("qr")
                    .add("type", "QR").add("storeCode", "STORE001").add("tableNo", 1).build();
            given(jwtTokenProvider.parseToken("qr-jwt")).willReturn(claims);
            given(storeRepository.findByStoreCode("STORE001")).willReturn(Optional.of(store));
            given(storeTableRepository.findByStoreIdAndTableNo(any(), eq(1))).willReturn(Optional.of(storeTable));
            mockTokenCreation();

            AuthTokens result = authService.loginByQrToken("qr-jwt");

            assertThat(result.accessToken()).isEqualTo("access-token");
            assertThat(result.role()).isEqualTo("TABLE");
        }

        @Test
        @DisplayName("TC-AUTH-017: 무효한 QR 토큰 → INVALID_CREDENTIALS")
        void invalidToken() {
            given(jwtTokenProvider.validateToken("invalid")).willReturn(false);

            assertThatThrownBy(() -> authService.loginByQrToken("invalid"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS));
        }

        @Test
        @DisplayName("TC-AUTH-018: type=QR이 아닌 토큰 → INVALID_CREDENTIALS")
        void notQrType() {
            given(jwtTokenProvider.validateToken("access-jwt")).willReturn(true);
            var claims = io.jsonwebtoken.Jwts.claims().subject("1")
                    .add("tokenType", "ACCESS").add("role", "ADMIN").build();
            given(jwtTokenProvider.parseToken("access-jwt")).willReturn(claims);

            assertThatThrownBy(() -> authService.loginByQrToken("access-jwt"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS));
        }
    }

    @Nested
    @DisplayName("generateQrToken")
    class GenerateQrToken {

        @Test
        @DisplayName("TC-AUTH-019: QR 토큰 생성 성공")
        void success() {
            given(storeRepository.findById(1L)).willReturn(Optional.of(store));
            given(storeTableRepository.findByStoreIdAndTableNo(any(), eq(1))).willReturn(Optional.of(storeTable));
            given(jwtTokenProvider.createQrToken(any(), any())).willReturn("qr-token");
            given(jwtTokenProvider.getQrExpiration()).willReturn(600000L);

            QrTokenResponse result = authService.generateQrToken(1L, 1);

            assertThat(result.qrToken()).isEqualTo("qr-token");
            assertThat(result.expiresIn()).isEqualTo(600000L);
        }
    }

    @Nested
    @DisplayName("refreshToken")
    class RefreshTokenTest {

        @Test
        @DisplayName("TC-AUTH-020: Refresh Token 갱신 성공")
        void success() {
            given(jwtTokenProvider.validateToken("refresh-jwt")).willReturn(true);
            var claims = io.jsonwebtoken.Jwts.claims().subject("1")
                    .add("tokenType", "REFRESH").add("role", "ADMIN")
                    .add("storeId", 1L).add("adminId", 1L)
                    .add("username", "admin").add("storeName", "테스트매장").build();
            given(jwtTokenProvider.parseToken("refresh-jwt")).willReturn(claims);
            mockTokenCreation();

            AuthTokens result = authService.refreshToken("refresh-jwt");

            assertThat(result.accessToken()).isEqualTo("access-token");
            assertThat(result.refreshToken()).isEqualTo("refresh-token");
        }

        @Test
        @DisplayName("TC-AUTH-021: 무효한 Refresh Token → INVALID_CREDENTIALS")
        void invalidToken() {
            given(jwtTokenProvider.validateToken("invalid")).willReturn(false);

            assertThatThrownBy(() -> authService.refreshToken("invalid"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS));
        }

        @Test
        @DisplayName("TC-AUTH-022: Access Token으로 refresh 시도 → INVALID_CREDENTIALS")
        void accessTokenUsed() {
            given(jwtTokenProvider.validateToken("access-jwt")).willReturn(true);
            var claims = io.jsonwebtoken.Jwts.claims().subject("1")
                    .add("tokenType", "ACCESS").add("role", "ADMIN").build();
            given(jwtTokenProvider.parseToken("access-jwt")).willReturn(claims);

            assertThatThrownBy(() -> authService.refreshToken("access-jwt"))
                    .isInstanceOf(BusinessException.class)
                    .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS));
        }
    }
}
