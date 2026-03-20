package com.tableorder.order.repository;

import com.tableorder.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllBySessionId(Long sessionId);
    List<Order> findAllByStoreIdAndStatusNot(Long storeId, String status);
}
