package com.tableorder.menu.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.domain.Category;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.dto.*;
import com.tableorder.menu.repository.CategoryRepository;
import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository, StoreRepository storeRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public CategoryResponse createCategory(Long storeId, CategoryCreateRequest request) {
        storeRepository.findById(storeId).orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        if (categoryRepository.findByStoreIdAndName(storeId, request.name()).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }
        int order = categoryRepository.countByStoreId(storeId) + 1;
        Category cat = categoryRepository.save(new Category(storeId, request.name(), order));
        return toCategoryResponse(cat);
    }

    public List<CategoryResponse> getCategories(Long storeId) {
        return categoryRepository.findAllByStoreIdOrderByDisplayOrder(storeId).stream()
                .map(this::toCategoryResponse).toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        cat.updateName(request.name());
        return toCategoryResponse(cat);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        if (menuRepository.existsByCategoryId(categoryId)) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_MENUS);
        }
        categoryRepository.delete(cat);
    }

    @Transactional
    public MenuResponse createMenu(Long storeId, MenuCreateRequest request) {
        storeRepository.findById(storeId).orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        int order = menuRepository.countByStoreId(storeId) + 1;
        Menu menu = menuRepository.save(new Menu(storeId, request.categoryId(), request.name(),
                request.price(), request.description(), request.imageUrl(), order));
        return toMenuResponse(menu);
    }

    public List<MenuResponse> getAllMenus(Long storeId) {
        return menuRepository.findAllByStoreIdOrderByDisplayOrder(storeId).stream()
                .map(this::toMenuResponse).toList();
    }

    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest request) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
        menu.update(request.name(), request.price(), request.description(), request.categoryId(), request.imageUrl());
        return toMenuResponse(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
        menu.softDelete();
    }

    @Transactional
    public void updateMenuOrder(Long storeId, List<MenuOrderItem> items) {
        List<Long> ids = items.stream().map(MenuOrderItem::menuId).toList();
        Map<Long, Menu> menuMap = menuRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Menu::getId, m -> m));
        items.forEach(item -> {
            Menu menu = menuMap.get(item.menuId());
            if (menu != null) menu.setDisplayOrder(item.displayOrder());
        });
    }

    public List<CategoryWithMenusResponse> getMenusForCustomer(Long storeId) {
        List<Category> categories = categoryRepository.findAllByStoreIdOrderByDisplayOrder(storeId);
        return categories.stream().map(cat -> {
            List<MenuResponse> menus = menuRepository.findAllByCategoryIdAndAvailableTrueOrderByDisplayOrder(cat.getId())
                    .stream().map(this::toMenuResponse).toList();
            return new CategoryWithMenusResponse(cat.getId(), cat.getName(), menus);
        }).toList();
    }

    private CategoryResponse toCategoryResponse(Category cat) {
        return new CategoryResponse(cat.getId(), cat.getName(), cat.getDisplayOrder());
    }

    private MenuResponse toMenuResponse(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getDescription(), menu.getImageUrl(), menu.getCategoryId(), menu.getDisplayOrder());
    }
}
