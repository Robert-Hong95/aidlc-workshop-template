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
class TableAuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private AuthService authService;

    @Test
    @DisplayName("TC-AUTH-016: POST /api/customer/auth/login 성공")
    void login_success() throws Exception {
        given(authService.loginTable(any())).willReturn(
                new TableLoginResponse("table-jwt", 1L, "테스트매장", 1L, 1));

        mockMvc.perform(post("/api/customer/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TableLoginRequest("STORE01", 1, "table123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("table-jwt"))
                .andExpect(jsonPath("$.data.tableNo").value(1));
    }

    @Test
    @DisplayName("TC-AUTH-017: POST /api/customer/auth/login 실패 - 잘못된 인증")
    void login_invalidCredentials() throws Exception {
        given(authService.loginTable(any())).willThrow(new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        mockMvc.perform(post("/api/customer/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TableLoginRequest("INVALID", 1, "wrong"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}
