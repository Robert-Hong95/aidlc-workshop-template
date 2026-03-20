package com.tableorder.order.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_history")
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "order_data", nullable = false, columnDefinition = "JSON")
    private String orderData;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    protected OrderHistory() {}

    public OrderHistory(Long storeId, Long tableId, Long sessionId, String orderData,
                        Integer totalAmount, LocalDateTime orderedAt) {
        this.storeId = storeId;
        this.tableId = tableId;
        this.sessionId = sessionId;
        this.orderData = orderData;
        this.totalAmount = totalAmount;
        this.orderedAt = orderedAt;
        this.completedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getOrderData() { return orderData; }
    public Integer getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderedAt() { return orderedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
