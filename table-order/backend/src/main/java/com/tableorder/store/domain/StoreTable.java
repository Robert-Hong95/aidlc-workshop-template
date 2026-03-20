package com.tableorder.store.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_tables", uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "table_no"}))
public class StoreTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "table_no", nullable = false)
    private int tableNo;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected StoreTable() {}

    public StoreTable(Long storeId, int tableNo, String password) {
        this.storeId = storeId;
        this.tableNo = tableNo;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public int getTableNo() { return tableNo; }
    public String getPassword() { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
