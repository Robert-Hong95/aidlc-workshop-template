package com.tableorder.menu.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    protected Category() {}

    public Category(Long storeId, String name, int displayOrder) {
        this.storeId = storeId;
        this.name = name;
        this.displayOrder = displayOrder;
    }

    public void updateName(String name) { this.name = name; }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public String getName() { return name; }
    public int getDisplayOrder() { return displayOrder; }
}
