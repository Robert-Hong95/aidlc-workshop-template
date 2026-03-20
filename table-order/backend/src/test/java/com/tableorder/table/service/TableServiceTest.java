package com.tableorder.table.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderItem;
import com.tableorder.order.repository.OrderItemRepository;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.store.domain.Store;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.table.domain.OrderHistory;
import com.tableorder.table.domain.TableSession;
import com.tableorder.table.dto.*;
import com.tableorder.table.repository.OrderHistoryRepository;
import com.tableorder.table.repository.TableSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
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
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private OrderHistoryRepository orderHistoryRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ObjectMapper objectMapper;

    // --- setupTable ---
    @Test
    void setupTable_정상설정() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(storeTableRepository.findByStoreIdAndTableNo(1L, 1)).willReturn(Optional.empty());
        given(passwordEncoder.encode("Pass1234!")).willReturn("encoded");
        given(storeTableRepository.save(any(StoreTable.class))).willAnswer(inv -> inv.getArgument(0));

        TableResponse result = tableService.setupTable(1L, new TableSetupRequest(1, "Pass1234!"));

        assertThat(result.tableNo()).isEqualTo(1);
        then(storeTableRepository).should().save(any(StoreTable.class));
    }

    @Test
    void setupTable_중복테이블번호_예외() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(storeTableRepository.findByStoreIdAndTableNo(1L, 1)).willReturn(Optional.of(new StoreTable(1L, 1, "pw")));

        assertThatThrownBy(() -> tableService.setupTable(1L, new TableSetupRequest(1, "Pass1234!")))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.DUPLICATE_TABLE_NO);
    }

    @Test
    void setupTable_없는매장_예외() {
        given(storeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.setupTable(999L, new TableSetupRequest(1, "Pass1234!")))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.STORE_NOT_FOUND);
    }

    // --- getTables ---
    @Test
    void getTables_목록조회_활성세션포함() {
        StoreTable t1 = new StoreTable(1L, 1, "pw");
        StoreTable t2 = new StoreTable(1L, 2, "pw");
        given(storeTableRepository.findAllByStoreId(1L)).willReturn(List.of(t1, t2));
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(any())).willReturn(Optional.empty());

        List<TableResponse> result = tableService.getTables(1L);

        assertThat(result).hasSize(2);
    }

    // --- startSession ---
    @Test
    void startSession_새세션생성() {
        given(storeTableRepository.findById(1L)).willReturn(Optional.of(new StoreTable(1L, 1, "pw")));
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.empty());
        given(tableSessionRepository.save(any(TableSession.class))).willAnswer(inv -> inv.getArgument(0));

        TableSession result = tableService.startSession(1L);

        assertThat(result.isActive()).isTrue();
        then(tableSessionRepository).should().save(any(TableSession.class));
    }

    @Test
    void startSession_이미활성세션_기존반환() {
        TableSession existing = TableSession.start(1L);
        given(storeTableRepository.findById(1L)).willReturn(Optional.of(new StoreTable(1L, 1, "pw")));
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(existing));

        TableSession result = tableService.startSession(1L);

        assertThat(result).isSameAs(existing);
        then(tableSessionRepository).should(never()).save(any());
    }

    // --- endSession ---
    @Test
    void endSession_정상종료_주문이력이동() throws Exception {
        TableSession session = TableSession.start(1L);
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));
        given(orderRepository.findAllBySessionId(any())).willReturn(Collections.emptyList());

        tableService.endSession(1L);

        assertThat(session.isActive()).isFalse();
    }

    @Test
    void endSession_활성세션없음_예외() {
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.endSession(1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.SESSION_NOT_FOUND);
    }

    @Test
    void endSession_주문없는세션_정상종료() {
        TableSession session = TableSession.start(1L);
        given(tableSessionRepository.findByTableIdAndEndedAtIsNull(1L)).willReturn(Optional.of(session));
        given(orderRepository.findAllBySessionId(any())).willReturn(Collections.emptyList());

        tableService.endSession(1L);

        assertThat(session.isActive()).isFalse();
        then(orderHistoryRepository).should(never()).saveAll(anyList());
    }

    // --- getOrderHistory ---
    @Test
    void getOrderHistory_첫페이지() {
        OrderHistory h1 = new OrderHistory(1L, 1L, 1L, "{}", 10000, LocalDateTime.now());
        OrderHistory h2 = new OrderHistory(1L, 1L, 1L, "{}", 20000, LocalDateTime.now());
        OrderHistory h3 = new OrderHistory(1L, 1L, 1L, "{}", 30000, LocalDateTime.now());
        given(orderHistoryRepository.findByTableIdAndCriteria(eq(1L), isNull(), isNull(), isNull(), eq(3))).willReturn(List.of(h3, h2, h1));

        OrderHistoryPage result = tableService.getOrderHistory(1L, null, null, null, 2);

        assertThat(result.items()).hasSize(2);
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    void getOrderHistory_마지막페이지() {
        OrderHistory h1 = new OrderHistory(1L, 1L, 1L, "{}", 10000, LocalDateTime.now());
        given(orderHistoryRepository.findByTableIdAndCriteria(eq(1L), isNull(), isNull(), eq(5L), eq(3))).willReturn(List.of(h1));

        OrderHistoryPage result = tableService.getOrderHistory(1L, null, null, 5L, 2);

        assertThat(result.items()).hasSize(1);
        assertThat(result.hasNext()).isFalse();
    }
}
