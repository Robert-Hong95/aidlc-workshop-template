package com.tableorder.order.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderItem;
import com.tableorder.order.dto.*;
import com.tableorder.order.repository.OrderItemRepository;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.store.domain.Store;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.table.domain.TableSession;
import com.tableorder.table.service.TableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks private OrderService orderService;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private StoreRepository storeRepository;
    @Mock private StoreTableRepository storeTableRepository;
    @Mock private TableService tableService;
    @Mock private SseEmitterService sseEmitterService;

    // --- createOrder ---
    @Test
    void createOrder_정상생성() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(storeTableRepository.findById(1L)).willReturn(Optional.of(new StoreTable(1L, 1, "pw")));
        TableSession session = TableSession.start(1L);
        given(tableService.startSession(1L)).willReturn(session);
        Menu menu = new Menu(1L, 10L, "아메리카노", 4500, null, null, 1);
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
        given(orderRepository.save(any(Order.class))).willAnswer(inv -> inv.getArgument(0));
        given(orderItemRepository.saveAll(anyList())).willAnswer(inv -> inv.getArgument(0));

        OrderResponse result = orderService.createOrder(1L, 1L,
                new OrderCreateRequest(List.of(new OrderItemRequest(1L, 2))));

        assertThat(result.totalAmount()).isEqualTo(9000);
        assertThat(result.status()).isEqualTo("PENDING");
        then(sseEmitterService).should().publishToStore(eq(1L), any());
    }

    @Test
    void createOrder_메뉴없음_예외() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(storeTableRepository.findById(1L)).willReturn(Optional.of(new StoreTable(1L, 1, "pw")));
        given(tableService.startSession(1L)).willReturn(TableSession.start(1L));
        given(menuRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(1L, 1L,
                new OrderCreateRequest(List.of(new OrderItemRequest(999L, 1)))))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.MENU_NOT_FOUND);
    }

    // --- updateOrderStatus ---
    @Test
    void updateOrderStatus_정상전이() throws Exception {
        Order order = new Order(1L, 1L, 1L, 10000);
        setId(order, 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(orderItemRepository.findAllByOrderId(1L)).willReturn(Collections.emptyList());

        OrderResponse result = orderService.updateOrderStatus(1L, "CONFIRMED");

        assertThat(result.status()).isEqualTo("CONFIRMED");
        then(sseEmitterService).should().publishToTable(eq(1L), eq(1L), any());
    }

    @Test
    void updateOrderStatus_잘못된전이_예외() throws Exception {
        Order order = new Order(1L, 1L, 1L, 10000);
        setId(order, 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrderStatus(1L, "COMPLETED"))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_ORDER_STATUS);
    }

    // --- deleteOrder (admin) ---
    @Test
    void deleteOrder_관리자_정상삭제() throws Exception {
        Order order = new Order(1L, 1L, 1L, 10000);
        setId(order, 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        then(orderRepository).should().delete(order);
        then(sseEmitterService).should().publishToTable(eq(1L), eq(1L), any());
        then(sseEmitterService).should().publishToStore(eq(1L), any());
    }

    // --- deleteOrderByCustomer ---
    @Test
    void deleteOrderByCustomer_PENDING_정상삭제() throws Exception {
        Order order = new Order(1L, 1L, 1L, 10000);
        setId(order, 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        orderService.deleteOrderByCustomer(1L, 1L);

        then(orderRepository).should().delete(order);
    }

    @Test
    void deleteOrderByCustomer_다른테이블_예외() throws Exception {
        Order order = new Order(1L, 1L, 1L, 10000);
        setId(order, 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.deleteOrderByCustomer(1L, 999L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.ACCESS_DENIED);
    }

    @Test
    void deleteOrderByCustomer_PENDING아님_예외() throws Exception {
        Order order = new Order(1L, 1L, 1L, 10000);
        order.updateStatus("CONFIRMED");
        setId(order, 1L);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.deleteOrderByCustomer(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_ORDER_STATUS);
    }

    // --- getOrdersBySession ---
    @Test
    void getOrdersBySession_조회() {
        given(orderRepository.findAllBySessionId(1L)).willReturn(Collections.emptyList());

        List<OrderResponse> result = orderService.getOrdersBySession(1L);

        assertThat(result).isEmpty();
    }

    // --- getActiveOrdersByStore ---
    @Test
    void getActiveOrdersByStore_조회() {
        given(orderRepository.findAllByStoreIdAndStatusNot(1L, Order.COMPLETED)).willReturn(Collections.emptyList());

        List<OrderResponse> result = orderService.getActiveOrdersByStore(1L);

        assertThat(result).isEmpty();
    }

    private void setId(Object entity, Long id) throws Exception {
        var field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }
}
