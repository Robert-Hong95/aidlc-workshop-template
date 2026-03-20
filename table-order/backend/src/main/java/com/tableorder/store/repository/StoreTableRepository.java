package com.tableorder.store.repository;

import com.tableorder.store.domain.StoreTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {
    Optional<StoreTable> findByStoreIdAndTableNo(Long storeId, Integer tableNo);
    List<StoreTable> findAllByStoreId(Long storeId);
}
