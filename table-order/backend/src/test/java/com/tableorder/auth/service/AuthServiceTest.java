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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks private AuthService authService;
    @Mock private StoreRepository storeRepository;
    @Mock private AdminRepository adminRepository;
    @Mock private StoreTableRepository storeTableRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;

    private Store store;
    private Admin admin;
    private StoreTable storeTable;

    @BeforeEach
    void setUp() throws Exception {
        store = new Store("STORE01", "테스트매장");
        setId(store, 1L);
        admin = new Admin(1L, "admin", "encodedPassword");
        setId(admin, 1L);
        storeTable = new StoreTable(1L, 1, "encodedTablePass");
        setId(storeTable, 1L);
    }

    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    // === loginAdmin ===

    @Test
    @DisplayName("TC-AUTH-001: 유효한 정보로 관리자 로그인 성공")
    void loginAdmin_success() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(adminRepository.findByStoreIdAndUsername(any(), eq("admin"))).willReturn(Optional.of(admin));
        given(passwordEncoder.matches("pass1234", "encodedPassword")).willReturn(true);
        given(jwtTokenProvider.createToken(any(), any())).willReturn("jwt-token");

        AdminLoginResponse response = authService.loginAdmin(new AdminLoginRequest("STORE01", "admin", "pass1234"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.storeName()).isEqualTo("테스트매장");
        assertThat(response.username()).isEqualTo("admin");
    }

    @Test
    @DisplayName("TC-AUTH-002: 존재하지 않는 매장 코드로 로그인 실패")
    void loginAdmin_storeNotFound() {
        given(storeRepository.findByStoreCode("INVALID")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.loginAdmin(new AdminLoginRequest("INVALID", "admin", "pass1234")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_CREDENTIALS.getMessage());
    }

    @Test
    @DisplayName("TC-AUTH-003: 존재하지 않는 username으로 로그인 실패")
    void loginAdmin_usernameNotFound() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(adminRepository.findByStoreIdAndUsername(1L, "unknown")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.loginAdmin(new AdminLoginRequest("STORE01", "unknown", "pass1234")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_CREDENTIALS.getMessage());
    }

    @Test
    @DisplayName("TC-AUTH-004: 잘못된 비밀번호로 로그인 실패")
    void loginAdmin_wrongPassword() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(adminRepository.findByStoreIdAndUsername(1L, "admin")).willReturn(Optional.of(admin));
        given(passwordEncoder.matches("wrongpass", "encodedPassword")).willReturn(false);

        assertThatThrownBy(() -> authService.loginAdmin(new AdminLoginRequest("STORE01", "admin", "wrongpass")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_CREDENTIALS.getMessage());
    }

    // === registerAdmin ===

    @Test
    @DisplayName("TC-AUTH-005: 유효한 정보로 관리자 등록 성공")
    void registerAdmin_success() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(adminRepository.findByStoreIdAndUsername(1L, "newadmin")).willReturn(Optional.empty());
        given(passwordEncoder.encode("pass1234")).willReturn("encodedPass");
        given(adminRepository.save(any(Admin.class))).willAnswer(invocation -> {
            Admin saved = invocation.getArgument(0);
            setId(saved, 2L);
            return saved;
        });

        AdminRegisterResponse response = authService.registerAdmin(new AdminRegisterRequest("STORE01", "newadmin", "pass1234"));

        assertThat(response.adminId()).isEqualTo(2L);
        assertThat(response.storeId()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("newadmin");
    }

    @Test
    @DisplayName("TC-AUTH-006: 존재하지 않는 매장에 관리자 등록 실패")
    void registerAdmin_storeNotFound() {
        given(storeRepository.findByStoreCode("INVALID")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.registerAdmin(new AdminRegisterRequest("INVALID", "admin", "pass1234")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.STORE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("TC-AUTH-007: 동일 매장 내 중복 username 등록 실패")
    void registerAdmin_duplicateUsername() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(adminRepository.findByStoreIdAndUsername(1L, "admin")).willReturn(Optional.of(admin));

        assertThatThrownBy(() -> authService.registerAdmin(new AdminRegisterRequest("STORE01", "admin", "pass1234")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_ADMIN.getMessage());
    }

    // === loginTable ===

    @Test
    @DisplayName("TC-AUTH-008: 유효한 정보로 테이블 인증 성공")
    void loginTable_success() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(storeTableRepository.findByStoreIdAndTableNo(1L, 1)).willReturn(Optional.of(storeTable));
        given(passwordEncoder.matches("table123", "encodedTablePass")).willReturn(true);
        given(jwtTokenProvider.createToken(any(), any())).willReturn("table-jwt-token");

        TableLoginResponse response = authService.loginTable(new TableLoginRequest("STORE01", 1, "table123"));

        assertThat(response.token()).isEqualTo("table-jwt-token");
        assertThat(response.storeId()).isEqualTo(1L);
        assertThat(response.storeName()).isEqualTo("테스트매장");
        assertThat(response.tableId()).isEqualTo(1L);
        assertThat(response.tableNo()).isEqualTo(1);
    }

    @Test
    @DisplayName("TC-AUTH-009: 존재하지 않는 매장으로 테이블 인증 실패")
    void loginTable_storeNotFound() {
        given(storeRepository.findByStoreCode("INVALID")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.loginTable(new TableLoginRequest("INVALID", 1, "table123")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_CREDENTIALS.getMessage());
    }

    @Test
    @DisplayName("TC-AUTH-010: 존재하지 않는 테이블 번호로 인증 실패")
    void loginTable_tableNotFound() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(storeTableRepository.findByStoreIdAndTableNo(1L, 99)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.loginTable(new TableLoginRequest("STORE01", 99, "table123")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_CREDENTIALS.getMessage());
    }

    @Test
    @DisplayName("TC-AUTH-011: 잘못된 비밀번호로 테이블 인증 실패")
    void loginTable_wrongPassword() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));
        given(storeTableRepository.findByStoreIdAndTableNo(1L, 1)).willReturn(Optional.of(storeTable));
        given(passwordEncoder.matches("wrongpass", "encodedTablePass")).willReturn(false);

        assertThatThrownBy(() -> authService.loginTable(new TableLoginRequest("STORE01", 1, "wrongpass")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_CREDENTIALS.getMessage());
    }
}
