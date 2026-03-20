package com.tableorder.order.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderStatus;
import com.tableorder.order.dto.*;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.domain.TableSession;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.store.repository.TableSessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private StoreTableRepository storeTableRepository;
    @Mock private TableSessionRepository tableSessionRepository;
    @Mock private SseEmitterService sseEmitterService;
    @InjectMocks private OrderService orderService;

    private <T> void setId(T entity, Long id) throws Exception {
        Field f = entity.getClass().getDeclaredField("id");
        f.setAccessible(true);
        f.set(entity, id);
    }

    private Menu makeMenu(Long id, String name, int price) throws Exception {
        Menu m = new Menu(1L, 1L, name, price, null, null, 0);
        setId(m, id);
        return m;
    }

    private Order makeOrder(Long id, Long sessionId, OrderStatus status) throws Exception {
        Order o = new Order(1L, 1L, sessionId, 10000);
        setId(o, id);
        if (status != OrderStatus.PENDING) {
            Field f = Order.class.getDeclaredField("status");
            f.setAccessible(true);
            f.set(o, status);
        }
        return o;
    }

    @Test @DisplayName("TC-ORD-001: createOrder 성공")
    void createOrder_success() throws Exception {
        StoreTable table = new StoreTable(1L, 1, "pw"); setId(table, 1L);
        given(storeTableRepository.findById(1L)).willReturn(Optional.of(table));
        TableSession session = new TableSession(1L); setId(session, 10L);
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));
        given(menuRepository.findById(1L)).willReturn(Optional.of(makeMenu(1L, "아메리카노", 4500)));
        given(orderRepository.save(any())).willAnswer(inv -> { Order o = inv.getArgument(0); setId(o, 1L); return o; });

        OrderResponse result = orderService.createOrder(1L, 1L,
                new OrderCreateRequest(List.of(new OrderItemRequest(1L, 2))));

        assertThat(result.totalAmount()).isEqualTo(9000);
        assertThat(result.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.items()).hasSize(1);
    }

    @Test @DisplayName("TC-ORD-002: createOrder 세션 자동 생성")
    void createOrder_autoSession() throws Exception {
        StoreTable table = new StoreTable(1L, 1, "pw"); setId(table, 1L);
        given(storeTableRepository.findById(1L)).willReturn(Optional.of(table));
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.empty());
        TableSession newSession = new TableSession(1L); setId(newSession, 20L);
        given(tableSessionRepository.save(any())).willReturn(newSession);
        given(menuRepository.findById(1L)).willReturn(Optional.of(makeMenu(1L, "라떼", 5000)));
        given(orderRepository.save(any())).willAnswer(inv -> { Order o = inv.getArgument(0); setId(o, 1L); return o; });

        OrderResponse result = orderService.createOrder(1L, 1L,
                new OrderCreateRequest(List.of(new OrderItemRequest(1L, 1))));

        assertThat(result.sessionId()).isEqualTo(20L);
        verify(tableSessionRepository).save(any());
    }

    @Test @DisplayName("TC-ORD-003: getOrdersByTable 성공")
    void getOrdersByTable_success() throws Exception {
        TableSession session = new TableSession(1L); setId(session, 10L);
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));
        Order o = makeOrder(1L, 10L, OrderStatus.PENDING);
        given(orderRepository.findBySessionIdOrderByCreatedAtDesc(10L)).willReturn(List.of(o));

        List<OrderResponse> result = orderService.getOrdersByTable(1L, 1L);

        assertThat(result).hasSize(1);
    }

    @Test @DisplayName("TC-ORD-004: getActiveOrders 성공")
    void getActiveOrders_success() throws Exception {
        Order o = makeOrder(1L, 10L, OrderStatus.PENDING);
        given(orderRepository.findByStoreIdAndStatusIn(1L, List.of(OrderStatus.PENDING, OrderStatus.PREPARING)))
                .willReturn(List.of(o));

        List<OrderResponse> result = orderService.getActiveOrders(1L);

        assertThat(result).hasSize(1);
    }

    @Test @DisplayName("TC-ORD-005: updateOrderStatus 성공")
    void updateOrderStatus_success() throws Exception {
        Order o = makeOrder(1L, 10L, OrderStatus.PENDING);
        given(orderRepository.findById(1L)).willReturn(Optional.of(o));

        OrderResponse result = orderService.updateOrderStatus(1L, new OrderStatusRequest(OrderStatus.PREPARING));

        assertThat(result.status()).isEqualTo(OrderStatus.PREPARING);
    }

    @Test @DisplayName("TC-ORD-006: updateOrderStatus 역방향 실패")
    void updateOrderStatus_invalidTransition() throws Exception {
        Order o = makeOrder(1L, 10L, OrderStatus.PREPARING);
        given(orderRepository.findById(1L)).willReturn(Optional.of(o));

        assertThatThrownBy(() -> orderService.updateOrderStatus(1L, new OrderStatusRequest(OrderStatus.PENDING)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_ORDER_STATUS);
    }

    @Test @DisplayName("TC-ORD-007: deleteOrderByAdmin 성공")
    void deleteOrderByAdmin_success() throws Exception {
        Order o = makeOrder(1L, 10L, OrderStatus.PREPARING);
        given(orderRepository.findById(1L)).willReturn(Optional.of(o));

        orderService.deleteOrderByAdmin(1L);

        verify(orderRepository).delete(o);
    }

    @Test @DisplayName("TC-ORD-008: deleteOrderByCustomer 성공 (PENDING)")
    void deleteOrderByCustomer_success() throws Exception {
        Order o = makeOrder(1L, 10L, OrderStatus.PENDING);
        given(orderRepository.findById(1L)).willReturn(Optional.of(o));

        orderService.deleteOrderByCustomer(1L);

        verify(orderRepository).delete(o);
    }

    @Test @DisplayName("TC-ORD-009: deleteOrderByCustomer 실패 (PREPARING)")
    void deleteOrderByCustomer_notPending() throws Exception {
        Order o = makeOrder(1L, 10L, OrderStatus.PREPARING);
        given(orderRepository.findById(1L)).willReturn(Optional.of(o));

        assertThatThrownBy(() -> orderService.deleteOrderByCustomer(1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.ACCESS_DENIED);
    }

    @Test @DisplayName("TC-ORD-010: createOrder 실패 - 테이블 없음")
    void createOrder_tableNotFound() {
        given(storeTableRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(1L, 99L,
                new OrderCreateRequest(List.of(new OrderItemRequest(1L, 1)))))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.TABLE_NOT_FOUND);
    }
}
