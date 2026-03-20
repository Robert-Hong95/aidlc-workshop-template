package com.tableorder.order.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    public static final String PENDING = "PENDING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PREPARING = "PREPARING";
    public static final String COMPLETED = "COMPLETED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Order() {}

    public Order(Long storeId, Long tableId, Long sessionId, int totalAmount) {
        this.storeId = storeId;
        this.tableId = tableId;
        this.sessionId = sessionId;
        this.totalAmount = totalAmount;
        this.status = PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canTransitionTo(String newStatus) {
        return switch (this.status) {
            case PENDING -> CONFIRMED.equals(newStatus);
            case CONFIRMED -> PREPARING.equals(newStatus);
            case PREPARING -> COMPLETED.equals(newStatus);
            default -> false;
        };
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public Long getTableId() { return tableId; }
    public Long getSessionId() { return sessionId; }
    public int getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
