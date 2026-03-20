package com.tableorder.table.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderItem;
import com.tableorder.order.repository.OrderItemRepository;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.table.domain.OrderHistory;
import com.tableorder.table.domain.TableSession;
import com.tableorder.table.dto.*;
import com.tableorder.table.repository.OrderHistoryRepository;
import com.tableorder.table.repository.TableSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final StoreRepository storeRepository;
    private final StoreTableRepository storeTableRepository;
    private final TableSessionRepository tableSessionRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public TableService(StoreRepository storeRepository, StoreTableRepository storeTableRepository,
                        TableSessionRepository tableSessionRepository, OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository, OrderHistoryRepository orderHistoryRepository,
                        PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.storeRepository = storeRepository;
        this.storeTableRepository = storeTableRepository;
        this.tableSessionRepository = tableSessionRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public TableResponse setupTable(Long storeId, TableSetupRequest request) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        if (storeTableRepository.findByStoreIdAndTableNo(storeId, request.tableNo()).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_TABLE_NO);
        }
        StoreTable table = storeTableRepository.save(
                new StoreTable(storeId, request.tableNo(), passwordEncoder.encode(request.password())));
        return toTableResponse(table, null);
    }

    public List<TableResponse> getTables(Long storeId) {
        return storeTableRepository.findAllByStoreId(storeId).stream()
                .map(t -> {
                    Long sessionId = tableSessionRepository.findByTableIdAndEndedAtIsNull(t.getId())
                            .map(TableSession::getId).orElse(null);
                    return toTableResponse(t, sessionId);
                }).toList();
    }

    @Transactional
    public TableSession startSession(Long tableId) {
        storeTableRepository.findById(tableId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TABLE_NOT_FOUND));
        return tableSessionRepository.findByTableIdAndEndedAtIsNull(tableId)
                .orElseGet(() -> tableSessionRepository.save(TableSession.start(tableId)));
    }

    @Transactional
    public void endSession(Long tableId) {
        TableSession session = tableSessionRepository.findByTableIdAndEndedAtIsNull(tableId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));

        List<Order> orders = orderRepository.findAllBySessionId(session.getId());
        if (!orders.isEmpty()) {
            List<Long> orderIds = orders.stream().map(Order::getId).toList();
            List<OrderItem> allItems = orderItemRepository.findAllByOrderIdIn(orderIds);

            List<OrderHistory> histories = new ArrayList<>();
            for (Order order : orders) {
                List<OrderItem> items = allItems.stream()
                        .filter(i -> i.getOrderId().equals(order.getId())).toList();
                String json = serializeOrder(order, items);
                histories.add(new OrderHistory(order.getStoreId(), order.getTableId(),
                        session.getId(), json, order.getTotalAmount(), order.getCreatedAt()));
            }
            orderHistoryRepository.saveAll(histories);
            orderRepository.deleteAll(orders);
        }
        session.end();
    }

    public OrderHistoryPage getOrderHistory(Long tableId, LocalDateTime dateFrom, LocalDateTime dateTo, Long lastId, int size) {
        List<OrderHistory> results = orderHistoryRepository.findByTableIdAndCriteria(tableId, dateFrom, dateTo, lastId, size + 1);
        boolean hasNext = results.size() > size;
        List<OrderHistory> items = hasNext ? results.subList(0, size) : results;
        Long nextLastId = items.isEmpty() ? null : items.get(items.size() - 1).getId();
        return new OrderHistoryPage(
                items.stream().map(this::toHistoryResponse).toList(),
                hasNext, nextLastId);
    }

    private String serializeOrder(Order order, List<OrderItem> items) {
        try {
            return objectMapper.writeValueAsString(Map.of("orderId", order.getId(),
                    "totalAmount", order.getTotalAmount(), "status", order.getStatus(), "items", items));
        } catch (Exception e) {
            throw new RuntimeException("Order serialization failed", e);
        }
    }

    private TableResponse toTableResponse(StoreTable table, Long activeSessionId) {
        return new TableResponse(table.getId(), table.getTableNo(), activeSessionId, table.getCreatedAt());
    }

    private OrderHistoryResponse toHistoryResponse(OrderHistory h) {
        return new OrderHistoryResponse(h.getId(), h.getTableId(), h.getTotalAmount(),
                h.getOrderData(), h.getOrderedAt(), h.getCompletedAt());
    }
}
