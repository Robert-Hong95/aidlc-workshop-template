package com.tableorder.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderHistory;
import com.tableorder.order.domain.OrderStatus;
import com.tableorder.order.repository.OrderHistoryRepository;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.domain.TableSession;
import com.tableorder.store.dto.*;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.store.repository.TableSessionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TableService {

    private final StoreRepository storeRepository;
    private final StoreTableRepository storeTableRepository;
    private final TableSessionRepository tableSessionRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TableService(StoreRepository storeRepository, StoreTableRepository storeTableRepository,
                        TableSessionRepository tableSessionRepository, OrderHistoryRepository orderHistoryRepository,
                        OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.storeRepository = storeRepository;
        this.storeTableRepository = storeTableRepository;
        this.tableSessionRepository = tableSessionRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TableResponse setupTable(Long storeId, TableSetupRequest request) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        storeTableRepository.findByStoreIdAndTableNo(storeId, request.tableNo())
                .ifPresent(t -> { throw new BusinessException(ErrorCode.DUPLICATE_TABLE_NO); });
        StoreTable table = storeTableRepository.save(
                new StoreTable(storeId, request.tableNo(), passwordEncoder.encode(request.password())));
        return new TableResponse(table.getId(), table.getStoreId(), table.getTableNo(), false);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> getTables(Long storeId) {
        return storeTableRepository.findAllByStoreId(storeId).stream()
                .map(t -> new TableResponse(t.getId(), t.getStoreId(), t.getTableNo(),
                        tableSessionRepository.findByTableIdAndEndedAtIsNull(t.getId()).isPresent()))
                .toList();
    }

    public void endSession(Long tableId) {
        TableSession session = tableSessionRepository.findByTableIdAndEndedAtIsNull(tableId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));

        List<Order> orders = orderRepository.findBySessionId(session.getId());
        boolean hasIncomplete = orders.stream()
                .anyMatch(o -> o.getStatus() != OrderStatus.COMPLETED);
        if (hasIncomplete) {
            throw new BusinessException(ErrorCode.HAS_INCOMPLETE_ORDERS);
        }

        if (!orders.isEmpty()) {
            List<OrderHistory> histories = orders.stream().map(o -> {
                String json;
                try { json = objectMapper.writeValueAsString(o); }
                catch (JsonProcessingException e) { json = "{}"; }
                return new OrderHistory(o.getStoreId(), o.getTableId(), session.getId(),
                        json, o.getTotalAmount(), o.getCreatedAt());
            }).toList();
            orderHistoryRepository.saveAll(histories);
            orderRepository.deleteAll(orders);
        }

        session.end();
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryResponse> getOrderHistory(Long tableId, LocalDate dateFrom, LocalDate dateTo) {
        LocalDateTime from = dateFrom.atStartOfDay();
        LocalDateTime to = dateTo.plusDays(1).atStartOfDay();
        return orderHistoryRepository.findByTableIdAndCompletedAtBetweenOrderByCompletedAtDesc(tableId, from, to)
                .stream()
                .map(h -> new OrderHistoryResponse(h.getId(), h.getOrderData(), h.getTotalAmount(),
                        h.getOrderedAt(), h.getCompletedAt()))
                .toList();
    }
}
