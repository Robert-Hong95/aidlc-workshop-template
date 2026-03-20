package com.tableorder.store.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_code", nullable = false, unique = true, length = 50)
    private String storeCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Store() {}

    public Store(String storeCode, String name) {
        this.storeCode = storeCode;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getStoreCode() { return storeCode; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }
}
