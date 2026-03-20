package com.tableorder.order.repository;

import com.tableorder.order.domain.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByTableIdAndCompletedAtBetweenOrderByCompletedAtDesc(
            Long tableId, LocalDateTime from, LocalDateTime to);
}
