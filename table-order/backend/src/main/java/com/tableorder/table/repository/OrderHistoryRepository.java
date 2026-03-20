package com.tableorder.table.repository;

import com.tableorder.table.domain.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    @Query("SELECT oh FROM OrderHistory oh WHERE oh.tableId = :tableId" +
           " AND (:dateFrom IS NULL OR oh.completedAt >= :dateFrom)" +
           " AND (:dateTo IS NULL OR oh.completedAt <= :dateTo)" +
           " AND (:lastId IS NULL OR oh.id < :lastId)" +
           " ORDER BY oh.id DESC")
    List<OrderHistory> findByTableIdAndCriteria(
            @Param("tableId") Long tableId,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("lastId") Long lastId,
            @Param("size") int size);
}
