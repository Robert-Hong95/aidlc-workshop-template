package com.tableorder.menu.repository;

import com.tableorder.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStoreIdAndAvailableTrueOrderByDisplayOrder(Long storeId);
    List<Menu> findAllByStoreIdOrderByDisplayOrder(Long storeId);
    List<Menu> findAllByCategoryIdAndAvailableTrueOrderByDisplayOrder(Long categoryId);
    int countByStoreId(Long storeId);
    boolean existsByCategoryId(Long categoryId);
}
