package com.tableorder.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.dto.*;
import com.tableorder.menu.service.FileStorageService;
import com.tableorder.menu.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MenuControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private MenuService menuService;
    @MockitoBean private FileStorageService fileStorageService;

    @Test @DisplayName("TC-CTRL-001: POST /api/admin/stores/{storeId}/categories 성공")
    @WithMockUser(roles = "ADMIN")
    void createCategory() throws Exception {
        given(menuService.createCategory(eq(1L), any())).willReturn(new CategoryResponse(1L, 1L, "음료", 0));

        mockMvc.perform(post("/api/admin/stores/1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryCreateRequest("음료"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("음료"));
    }

    @Test @DisplayName("TC-CTRL-002: POST /api/admin/stores/{storeId}/menus 성공")
    @WithMockUser(roles = "ADMIN")
    void createMenu() throws Exception {
        given(menuService.createMenu(eq(1L), any())).willReturn(
                new MenuResponse(1L, 1L, 1L, "아메리카노", 4500, null, null, 0, LocalDateTime.now()));

        mockMvc.perform(post("/api/admin/stores/1/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MenuCreateRequest(1L, "아메리카노", 4500, null, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("아메리카노"));
    }

    @Test @DisplayName("TC-CTRL-003: DELETE /api/admin/menus/{menuId} 성공")
    @WithMockUser(roles = "ADMIN")
    void deleteMenu() throws Exception {
        mockMvc.perform(delete("/api/admin/menus/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test @DisplayName("TC-CTRL-004: GET /api/customer/stores/{storeId}/menus 성공")
    @WithMockUser(roles = "CUSTOMER")
    void getMenusForCustomer() throws Exception {
        given(menuService.getMenusForCustomer(1L)).willReturn(List.of(
                new CategoryWithMenusResponse(1L, "음료", 0, List.of(
                        new MenuResponse(1L, 1L, 1L, "아메리카노", 4500, null, null, 0, LocalDateTime.now())))));

        mockMvc.perform(get("/api/customer/stores/1/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].categoryName").value("음료"));
    }

    @Test @DisplayName("TC-CTRL-005: POST /api/files/upload 성공")
    @WithMockUser(roles = "ADMIN")
    void uploadFile() throws Exception {
        given(fileStorageService.upload(any())).willReturn("uuid.jpg");

        mockMvc.perform(multipart("/api/files/upload")
                        .file(new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("uuid.jpg"));
    }

    @Test @DisplayName("TC-CTRL-006: POST /api/files/upload 실패 - 잘못된 형식")
    @WithMockUser(roles = "ADMIN")
    void uploadFile_invalidType() throws Exception {
        given(fileStorageService.upload(any())).willThrow(new BusinessException(ErrorCode.INVALID_FILE_TYPE));

        mockMvc.perform(multipart("/api/files/upload")
                        .file(new MockMultipartFile("file", "test.txt", "text/plain", new byte[]{1})))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test @DisplayName("TC-CTRL-007: GET /api/files/{filename} 성공")
    @WithMockUser
    void getFile() throws Exception {
        given(fileStorageService.loadAsResource("test.jpg")).willReturn(new ByteArrayResource(new byte[]{1, 2, 3}));

        mockMvc.perform(get("/api/files/test.jpg"))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("TC-CTRL-008: PUT /api/admin/stores/{storeId}/menus/order 성공")
    @WithMockUser(roles = "ADMIN")
    void updateMenuOrder() throws Exception {
        mockMvc.perform(put("/api/admin/stores/1/menus/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DisplayOrderRequest(List.of(2L, 1L)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
