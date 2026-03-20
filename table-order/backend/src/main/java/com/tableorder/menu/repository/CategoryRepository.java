package com.tableorder.menu.repository;

import com.tableorder.menu.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByStoreIdOrderByDisplayOrder(Long storeId);
    boolean existsByIdAndStoreId(Long id, Long storeId);
}
