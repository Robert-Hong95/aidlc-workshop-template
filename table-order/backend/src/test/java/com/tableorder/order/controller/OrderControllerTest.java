package com.tableorder.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tableorder.order.domain.OrderStatus;
import com.tableorder.order.dto.*;
import com.tableorder.order.service.OrderService;
import com.tableorder.order.service.SseEmitterService;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
class OrderControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private OrderService orderService;
    @MockitoBean private SseEmitterService sseEmitterService;

    private OrderResponse sampleOrder() {
        return new OrderResponse(1L, 1L, 1L, 10L, 9000, OrderStatus.PENDING,
                List.of(new OrderItemResponse(1L, "아메리카노", 2, 4500)), LocalDateTime.now());
    }

    @Test @DisplayName("TC-CTRL-001: POST /api/customer/.../orders 성공")
    @WithMockUser(roles = "CUSTOMER")
    void createOrder() throws Exception {
        given(orderService.createOrder(eq(1L), eq(1L), any())).willReturn(sampleOrder());

        mockMvc.perform(post("/api/customer/stores/1/tables/1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderCreateRequest(List.of(new OrderItemRequest(1L, 2))))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalAmount").value(9000));
    }

    @Test @DisplayName("TC-CTRL-002: GET /api/customer/.../orders 성공")
    @WithMockUser(roles = "CUSTOMER")
    void getOrders() throws Exception {
        given(orderService.getOrdersByTable(1L, 1L)).willReturn(List.of(sampleOrder()));

        mockMvc.perform(get("/api/customer/stores/1/tables/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].totalAmount").value(9000));
    }

    @Test @DisplayName("TC-CTRL-003: DELETE /api/customer/orders/{id} 성공")
    @WithMockUser(roles = "CUSTOMER")
    void cancelOrder() throws Exception {
        mockMvc.perform(delete("/api/customer/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test @DisplayName("TC-CTRL-004: GET /api/admin/stores/{id}/orders 성공")
    @WithMockUser(roles = "ADMIN")
    void activeOrders() throws Exception {
        given(orderService.getActiveOrders(1L)).willReturn(List.of(sampleOrder()));

        mockMvc.perform(get("/api/admin/stores/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].status").value("PENDING"));
    }

    @Test @DisplayName("TC-CTRL-005: PUT /api/admin/orders/{id}/status 성공")
    @WithMockUser(roles = "ADMIN")
    void updateStatus() throws Exception {
        OrderResponse updated = new OrderResponse(1L, 1L, 1L, 10L, 9000, OrderStatus.PREPARING,
                List.of(), LocalDateTime.now());
        given(orderService.updateOrderStatus(eq(1L), any())).willReturn(updated);

        mockMvc.perform(put("/api/admin/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequest(OrderStatus.PREPARING))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PREPARING"));
    }

    @Test @DisplayName("TC-CTRL-006: DELETE /api/admin/orders/{id} 성공")
    @WithMockUser(roles = "ADMIN")
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/api/admin/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test @DisplayName("TC-CTRL-007: GET /api/admin/stores/{id}/sse 성공")
    @WithMockUser(roles = "ADMIN")
    void adminSse() throws Exception {
        given(sseEmitterService.subscribe(eq(1L), eq("ADMIN"), any())).willReturn(new SseEmitter());

        mockMvc.perform(get("/api/admin/stores/1/sse"))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("TC-CTRL-008: GET /api/customer/.../sse 성공")
    @WithMockUser(roles = "CUSTOMER")
    void customerSse() throws Exception {
        given(sseEmitterService.subscribe(eq(1L), eq("CUSTOMER"), any())).willReturn(new SseEmitter());

        mockMvc.perform(get("/api/customer/stores/1/tables/1/sse"))
                .andExpect(status().isOk());
    }
}
