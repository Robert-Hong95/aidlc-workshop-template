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
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.table.domain.TableSession;
import com.tableorder.table.service.TableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final StoreTableRepository storeTableRepository;
    private final TableService tableService;
    private final SseEmitterService sseEmitterService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        MenuRepository menuRepository, StoreRepository storeRepository,
                        StoreTableRepository storeTableRepository, TableService tableService,
                        SseEmitterService sseEmitterService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
        this.storeTableRepository = storeTableRepository;
        this.tableService = tableService;
        this.sseEmitterService = sseEmitterService;
    }

    @Transactional
    public OrderResponse createOrder(Long storeId, Long tableId, OrderCreateRequest request) {
        storeRepository.findById(storeId).orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        storeTableRepository.findById(tableId).orElseThrow(() -> new BusinessException(ErrorCode.TABLE_NOT_FOUND));
        TableSession session = tableService.startSession(tableId);

        int totalAmount = 0;
        for (OrderItemRequest item : request.items()) {
            Menu menu = menuRepository.findById(item.menuId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
            totalAmount += menu.getPrice() * item.quantity();
        }

        Order order = orderRepository.save(new Order(storeId, tableId, session.getId(), totalAmount));

        List<OrderItem> items = request.items().stream().map(item -> {
            Menu menu = menuRepository.findById(item.menuId()).orElseThrow();
            return new OrderItem(order.getId(), menu.getId(), menu.getName(), item.quantity(), menu.getPrice());
        }).toList();
        orderItemRepository.saveAll(items);

        OrderResponse response = toResponse(order, items);
        sseEmitterService.publishToStore(storeId, Map.of("type", "NEW_ORDER", "order", response));
        return response;
    }

    public List<OrderResponse> getOrdersBySession(Long sessionId) {
        return orderRepository.findAllBySessionId(sessionId).stream()
                .map(o -> toResponse(o, orderItemRepository.findAllByOrderId(o.getId()))).toList();
    }

    public List<OrderResponse> getActiveOrdersByStore(Long storeId) {
        return orderRepository.findAllByStoreIdAndStatusNot(storeId, Order.COMPLETED).stream()
                .map(o -> toResponse(o, orderItemRepository.findAllByOrderId(o.getId()))).toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.canTransitionTo(status)) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }
        order.updateStatus(status);
        OrderResponse response = toResponse(order, orderItemRepository.findAllByOrderId(orderId));
        sseEmitterService.publishToTable(order.getStoreId(), order.getTableId(),
                Map.of("type", "STATUS_CHANGED", "order", response));
        return response;
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        orderRepository.delete(order);
        sseEmitterService.publishToTable(order.getStoreId(), order.getTableId(),
                Map.of("type", "ORDER_DELETED", "orderId", orderId));
        sseEmitterService.publishToStore(order.getStoreId(),
                Map.of("type", "ORDER_DELETED", "orderId", orderId));
    }

    @Transactional
    public void deleteOrderByCustomer(Long orderId, Long tableId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getTableId().equals(tableId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        if (!Order.PENDING.equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }
        orderRepository.delete(order);
        sseEmitterService.publishToStore(order.getStoreId(),
                Map.of("type", "ORDER_DELETED", "orderId", orderId));
    }

    private OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(i -> new OrderItemResponse(i.getId(), i.getMenuName(), i.getQuantity(), i.getUnitPrice())).toList();
        return new OrderResponse(order.getId(), order.getTableId(), order.getTotalAmount(),
                order.getStatus(), itemResponses, order.getCreatedAt());
    }
}
