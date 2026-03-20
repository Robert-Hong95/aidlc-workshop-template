package com.tableorder.order.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private Long storeId;
    @Column(nullable = false) private Long tableId;
    @Column(nullable = false) private Long sessionId;
    @Column(nullable = false) private int totalAmount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private OrderStatus status;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {}

    public Order(Long storeId, Long tableId, Long sessionId, int totalAmount) {
        this.storeId = storeId;
        this.tableId = tableId;
        this.sessionId = sessionId;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(OrderStatus newStatus) { this.status = newStatus; this.updatedAt = LocalDateTime.now(); }
    public void addItem(OrderItem item) { items.add(item); item.setOrder(this); }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public Long getTableId() { return tableId; }
    public Long getSessionId() { return sessionId; }
    public int getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<OrderItem> getItems() { return items; }
}
