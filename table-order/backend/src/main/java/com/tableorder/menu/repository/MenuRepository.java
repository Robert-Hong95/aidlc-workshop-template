package com.tableorder.menu.repository;

import com.tableorder.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m JOIN Category c ON m.categoryId = c.id WHERE m.storeId = :storeId AND m.deleted = false ORDER BY c.displayOrder, m.displayOrder")
    List<Menu> findAllActiveByStoreId(Long storeId);

    boolean existsByCategoryIdAndDeletedFalse(Long categoryId);
}
