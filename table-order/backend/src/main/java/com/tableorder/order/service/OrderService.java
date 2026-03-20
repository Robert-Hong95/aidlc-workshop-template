package com.tableorder.order.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderItem;
import com.tableorder.order.domain.OrderStatus;
import com.tableorder.order.dto.*;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.store.domain.TableSession;
import com.tableorder.store.repository.StoreTableRepository;
import com.tableorder.store.repository.TableSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreTableRepository storeTableRepository;
    private final TableSessionRepository tableSessionRepository;
    private final SseEmitterService sseEmitterService;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository,
                        StoreTableRepository storeTableRepository, TableSessionRepository tableSessionRepository,
                        SseEmitterService sseEmitterService) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.storeTableRepository = storeTableRepository;
        this.tableSessionRepository = tableSessionRepository;
        this.sseEmitterService = sseEmitterService;
    }

    public OrderResponse createOrder(Long storeId, Long tableId, OrderCreateRequest request) {
        storeTableRepository.findById(tableId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TABLE_NOT_FOUND));

        TableSession session = tableSessionRepository.findByTableIdAndEndedAtIsNull(tableId)
                .orElseGet(() -> tableSessionRepository.save(new TableSession(tableId)));

        int totalAmount = 0;
        Order order = new Order(storeId, tableId, session.getId(), 0);
        for (OrderItemRequest item : request.items()) {
            Menu menu = menuRepository.findById(item.menuId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
            order.addItem(new OrderItem(menu.getId(), menu.getName(), item.quantity(), menu.getPrice()));
            totalAmount += menu.getPrice() * item.quantity();
        }
        // set totalAmount via reflection-free approach: recreate
        Order finalOrder = new Order(storeId, tableId, session.getId(), totalAmount);
        order.getItems().forEach(finalOrder::addItem);
        finalOrder = orderRepository.save(finalOrder);

        sseEmitterService.publishToStore(storeId, new SseEvent("NEW_ORDER", toResponse(finalOrder)));
        return toResponse(finalOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByTable(Long storeId, Long tableId) {
        TableSession session = tableSessionRepository.findByTableIdAndEndedAtIsNull(tableId)
                .orElse(null);
        if (session == null) return List.of();
        return orderRepository.findBySessionIdOrderByCreatedAtDesc(session.getId()).stream()
                .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getActiveOrders(Long storeId) {
        return orderRepository.findByStoreIdAndStatusIn(storeId, List.of(OrderStatus.PENDING, OrderStatus.PREPARING))
                .stream().map(this::toResponse).toList();
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getStatus().canTransitionTo(request.status())) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }
        order.changeStatus(request.status());
        sseEmitterService.publishToStore(order.getStoreId(), new SseEvent("ORDER_STATUS_CHANGED", toResponse(order)));
        sseEmitterService.publishToTable(order.getStoreId(), order.getTableId(), new SseEvent("ORDER_STATUS_CHANGED", toResponse(order)));
        return toResponse(order);
    }

    public void deleteOrderByAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        orderRepository.delete(order);
        sseEmitterService.publishToStore(order.getStoreId(), new SseEvent("ORDER_DELETED", orderId));
        sseEmitterService.publishToTable(order.getStoreId(), order.getTableId(), new SseEvent("ORDER_DELETED", orderId));
    }

    public void deleteOrderByCustomer(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        orderRepository.delete(order);
        sseEmitterService.publishToStore(order.getStoreId(), new SseEvent("ORDER_DELETED", orderId));
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderItemResponse(i.getMenuId(), i.getMenuName(), i.getQuantity(), i.getUnitPrice()))
                .toList();
        return new OrderResponse(o.getId(), o.getStoreId(), o.getTableId(), o.getSessionId(),
                o.getTotalAmount(), o.getStatus(), items, o.getCreatedAt());
    }
}
