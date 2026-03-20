package com.tableorder.table.domain;

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

    public static TableSession start(Long tableId) {
        TableSession session = new TableSession();
        session.tableId = tableId;
        session.startedAt = LocalDateTime.now();
        return session;
    }

    public boolean isActive() { return endedAt == null; }

    public void end() { this.endedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getTableId() { return tableId; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getEndedAt() { return endedAt; }
}
