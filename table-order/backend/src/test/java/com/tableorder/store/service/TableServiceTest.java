package com.tableorder.store.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.order.domain.OrderHistory;
import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderStatus;
import com.tableorder.order.repository.OrderHistoryRepository;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.store.domain.Store;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.domain.TableSession;
import com.tableorder.store.dto.*;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.store.repository.TableSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks private TableService tableService;
    @Mock private StoreRepository storeRepository;
    @Mock private StoreTableRepository storeTableRepository;
    @Mock private TableSessionRepository tableSessionRepository;
    @Mock private OrderHistoryRepository orderHistoryRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private Store store;
    private StoreTable storeTable;
    private TableSession session;

    @BeforeEach
    void setUp() throws Exception {
        store = new Store("STORE01", "테스트매장");
        setId(store, 1L);
        storeTable = new StoreTable(1L, 1, "encoded");
        setId(storeTable, 1L);
        session = new TableSession(1L);
        setId(session, 1L);
    }

    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    @Test @DisplayName("TC-TABLE-001: 테이블 설정 성공")
    void setupTable_success() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(storeTableRepository.findByStoreIdAndTableNo(1L, 3)).willReturn(Optional.empty());
        given(passwordEncoder.encode("test1234")).willReturn("encoded");
        given(storeTableRepository.save(any(StoreTable.class))).willAnswer(inv -> {
            StoreTable t = inv.getArgument(0); setId(t, 3L); return t;
        });

        TableResponse res = tableService.setupTable(1L, new TableSetupRequest(3, "test1234"));
        assertThat(res.tableNo()).isEqualTo(3);
        assertThat(res.storeId()).isEqualTo(1L);
    }

    @Test @DisplayName("TC-TABLE-002: 중복 테이블 번호 설정 실패")
    void setupTable_duplicate() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(storeTableRepository.findByStoreIdAndTableNo(1L, 1)).willReturn(Optional.of(storeTable));

        assertThatThrownBy(() -> tableService.setupTable(1L, new TableSetupRequest(1, "test1234")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_TABLE_NO.getMessage());
    }

    @Test @DisplayName("TC-TABLE-003: 테이블 목록 조회")
    void getTables() {
        given(storeTableRepository.findAllByStoreId(1L)).willReturn(List.of(storeTable));
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));

        List<TableResponse> res = tableService.getTables(1L);
        assertThat(res).hasSize(1);
        assertThat(res.get(0).hasActiveSession()).isTrue();
    }

    @Test @DisplayName("TC-TABLE-004: 세션 종료 성공")
    void endSession_success() {
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));

        assertThatCode(() -> tableService.endSession(1L)).doesNotThrowAnyException();
        assertThat(session.isActive()).isFalse();
    }

    @Test @DisplayName("TC-TABLE-005: 세션 종료 실패 - 활성 세션 없음")
    void endSession_noSession() {
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.endSession(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.SESSION_NOT_FOUND.getMessage());
    }

    @Test @DisplayName("TC-TABLE-006: 과거 주문 내역 조회")
    void getOrderHistory() {
        OrderHistory history = new OrderHistory(1L, 1L, 1L, "{}", 10000, LocalDateTime.now());
        given(orderHistoryRepository.findByTableIdAndCompletedAtBetweenOrderByCompletedAtDesc(
                eq(1L), any(), any())).willReturn(List.of(history));

        List<OrderHistoryResponse> res = tableService.getOrderHistory(1L, LocalDate.now(), LocalDate.now());
        assertThat(res).hasSize(1);
    }

    @Test @DisplayName("TC-END-001: endSession 성공 - 완료 주문 이관")
    void endSession_withOrderMigration() throws Exception {
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));
        Order completedOrder = new Order(1L, 1L, 1L, 10000);
        setId(completedOrder, 1L);
        Field statusField = Order.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(completedOrder, OrderStatus.COMPLETED);
        given(orderRepository.findBySessionId(1L)).willReturn(List.of(completedOrder));

        tableService.endSession(1L);

        verify(orderHistoryRepository).saveAll(anyList());
        verify(orderRepository).deleteAll(anyList());
        assertThat(session.isActive()).isFalse();
    }

    @Test @DisplayName("TC-END-002: endSession 실패 - 미완료 주문")
    void endSession_incompleteOrders() throws Exception {
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));
        Order pendingOrder = new Order(1L, 1L, 1L, 5000);
        setId(pendingOrder, 2L);
        given(orderRepository.findBySessionId(1L)).willReturn(List.of(pendingOrder));

        assertThatThrownBy(() -> tableService.endSession(1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.HAS_INCOMPLETE_ORDERS);
    }
}
