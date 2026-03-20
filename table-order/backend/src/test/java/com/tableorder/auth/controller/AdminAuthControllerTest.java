package com.tableorder.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tableorder.auth.dto.*;
import com.tableorder.auth.service.AuthService;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminAuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private AuthService authService;

    @Test
    @DisplayName("TC-AUTH-012: POST /api/admin/auth/login 성공")
    void login_success() throws Exception {
        given(authService.loginAdmin(any())).willReturn(
                new AdminLoginResponse("jwt-token", 1L, "테스트매장", "admin"));

        mockMvc.perform(post("/api/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminLoginRequest("STORE01", "admin", "pass1234"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token"))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    @DisplayName("TC-AUTH-013: POST /api/admin/auth/login 실패 - 잘못된 인증")
    void login_invalidCredentials() throws Exception {
        given(authService.loginAdmin(any())).willThrow(new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        mockMvc.perform(post("/api/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminLoginRequest("STORE01", "admin", "wrong"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("TC-AUTH-014: POST /api/admin/auth/register 성공")
    void register_success() throws Exception {
        given(authService.registerAdmin(any())).willReturn(
                new AdminRegisterResponse(1L, 1L, "newadmin"));

        mockMvc.perform(post("/api/admin/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminRegisterRequest("STORE01", "newadmin", "pass1234"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("newadmin"));
    }

    @Test
    @DisplayName("TC-AUTH-015: POST /api/admin/auth/register 실패 - validation")
    void register_validationError() throws Exception {
        mockMvc.perform(post("/api/admin/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdminRegisterRequest("", "", ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
