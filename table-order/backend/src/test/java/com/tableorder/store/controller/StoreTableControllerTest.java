package com.tableorder.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.dto.*;
import com.tableorder.store.service.StoreService;
import com.tableorder.store.service.TableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreTableControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private StoreService storeService;
    @MockitoBean private TableService tableService;

    // === Store Controller ===

    @Test @DisplayName("TC-STORE-008: POST /api/admin/stores 성공")
    @WithMockUser(roles = "ADMIN")
    void createStore_success() throws Exception {
        given(storeService.createStore(any())).willReturn(
                new StoreResponse(1L, "NEW", "새매장", LocalDateTime.now()));

        mockMvc.perform(post("/api/admin/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StoreCreateRequest("NEW", "새매장"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.storeCode").value("NEW"));
    }

    @Test @DisplayName("TC-STORE-009: POST /api/admin/stores 실패 - 중복")
    @WithMockUser(roles = "ADMIN")
    void createStore_duplicate() throws Exception {
        given(storeService.createStore(any())).willThrow(new BusinessException(ErrorCode.DUPLICATE_STORE_CODE));

        mockMvc.perform(post("/api/admin/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StoreCreateRequest("STORE01", "매장"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test @DisplayName("TC-STORE-010: GET /api/admin/stores 성공")
    @WithMockUser(roles = "ADMIN")
    void getStores_success() throws Exception {
        given(storeService.getStores()).willReturn(List.of(
                new StoreResponse(1L, "S1", "매장1", LocalDateTime.now())));

        mockMvc.perform(get("/api/admin/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].storeCode").value("S1"));
    }

    // === Table Controller ===

    @Test @DisplayName("TC-TABLE-007: POST /api/admin/stores/{storeId}/tables 성공")
    @WithMockUser(roles = "ADMIN")
    void setupTable_success() throws Exception {
        given(tableService.setupTable(eq(1L), any())).willReturn(
                new TableResponse(1L, 1L, 3, false));

        mockMvc.perform(post("/api/admin/stores/1/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TableSetupRequest(3, "test1234"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tableNo").value(3));
    }

    @Test @DisplayName("TC-TABLE-008: POST /api/admin/tables/{tableId}/end-session 성공")
    @WithMockUser(roles = "ADMIN")
    void endSession_success() throws Exception {
        willDoNothing().given(tableService).endSession(1L);

        mockMvc.perform(post("/api/admin/tables/1/end-session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test @DisplayName("TC-TABLE-009: GET /api/admin/tables/{tableId}/order-history 성공")
    @WithMockUser(roles = "ADMIN")
    void orderHistory_success() throws Exception {
        given(tableService.getOrderHistory(eq(1L), any(), any())).willReturn(List.of(
                new OrderHistoryResponse(1L, "{}", 10000, LocalDateTime.now(), LocalDateTime.now())));

        mockMvc.perform(get("/api/admin/tables/1/order-history")
                        .param("dateFrom", "2026-03-01")
                        .param("dateTo", "2026-03-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].totalAmount").value(10000));
    }
}
