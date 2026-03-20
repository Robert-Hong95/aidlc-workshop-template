package com.tableorder.menu.repository;

import com.tableorder.menu.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByStoreIdOrderByDisplayOrder(Long storeId);
    Optional<Category> findByStoreIdAndName(Long storeId, String name);
    int countByStoreId(Long storeId);
}
