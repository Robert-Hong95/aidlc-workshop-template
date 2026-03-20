package com.tableorder.store.repository;

import com.tableorder.store.domain.TableSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TableSessionRepository extends JpaRepository<TableSession, Long> {
    Optional<TableSession> findByTableIdAndEndedAtIsNull(Long tableId);
}
