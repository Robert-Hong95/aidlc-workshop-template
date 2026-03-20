package com.tableorder.order.repository;

import com.tableorder.order.domain.Order;
import com.tableorder.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySessionIdOrderByCreatedAtDesc(Long sessionId);
    List<Order> findByStoreIdAndStatusIn(Long storeId, List<OrderStatus> statuses);
    List<Order> findBySessionId(Long sessionId);
}
