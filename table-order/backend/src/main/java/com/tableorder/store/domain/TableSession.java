package com.tableorder.store.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "table_sessions")
public class TableSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    protected TableSession() {}

    public TableSession(Long tableId) {
        this.tableId = tableId;
        this.startedAt = LocalDateTime.now();
    }

    public void end() { this.endedAt = LocalDateTime.now(); }
    public boolean isActive() { return this.endedAt == null; }

    public Long getId() { return id; }
    public Long getTableId() { return tableId; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getEndedAt() { return endedAt; }
}
