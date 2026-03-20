package com.tableorder.order.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false) private Long menuId;
    @Column(nullable = false, length = 100) private String menuName;
    @Column(nullable = false) private int quantity;
    @Column(nullable = false) private int unitPrice;

    protected OrderItem() {}

    public OrderItem(Long menuId, String menuName, int quantity, int unitPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    void setOrder(Order order) { this.order = order; }

    public Long getId() { return id; }
    public Long getMenuId() { return menuId; }
    public String getMenuName() { return menuName; }
    public int getQuantity() { return quantity; }
    public int getUnitPrice() { return unitPrice; }
}
