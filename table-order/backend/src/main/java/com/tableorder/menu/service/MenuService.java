package com.tableorder.menu.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.domain.Category;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.dto.*;
import com.tableorder.menu.repository.CategoryRepository;
import com.tableorder.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(Long storeId, CategoryCreateRequest request) {
        Category category = categoryRepository.save(new Category(storeId, request.name(), 0));
        return toCategoryResponse(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories(Long storeId) {
        return categoryRepository.findAllByStoreIdOrderByDisplayOrder(storeId).stream()
                .map(this::toCategoryResponse).toList();
    }

    public CategoryResponse updateCategory(Long categoryId, CategoryCreateRequest request) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        cat.updateName(request.name());
        return toCategoryResponse(cat);
    }

    public void deleteCategory(Long categoryId) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        if (menuRepository.existsByCategoryIdAndDeletedFalse(categoryId)) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_MENUS);
        }
        categoryRepository.delete(cat);
    }

    public void updateCategoryOrder(Long storeId, DisplayOrderRequest request) {
        Map<Long, Category> map = categoryRepository.findAllById(request.ids()).stream()
                .collect(Collectors.toMap(Category::getId, c -> c));
        IntStream.range(0, request.ids().size()).forEach(i -> {
            Category c = map.get(request.ids().get(i));
            if (c != null) c.updateDisplayOrder(i);
        });
    }

    public MenuResponse createMenu(Long storeId, MenuCreateRequest request) {
        if (!categoryRepository.existsById(request.categoryId())) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        validatePrice(request.price());
        Menu menu = menuRepository.save(new Menu(storeId, request.categoryId(), request.name(),
                request.price(), request.description(), request.imageUrl(), 0));
        return toMenuResponse(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long storeId) {
        return menuRepository.findAllActiveByStoreId(storeId).stream()
                .map(this::toMenuResponse).toList();
    }

    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest request) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
        validatePrice(request.price());
        menu.update(request.name(), request.price(), request.description(), request.imageUrl());
        return toMenuResponse(menu);
    }

    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
        menu.softDelete();
    }

    public void updateMenuOrder(Long storeId, DisplayOrderRequest request) {
        Map<Long, Menu> map = menuRepository.findAllById(request.ids()).stream()
                .collect(Collectors.toMap(Menu::getId, m -> m));
        IntStream.range(0, request.ids().size()).forEach(i -> {
            Menu m = map.get(request.ids().get(i));
            if (m != null) m.updateDisplayOrder(i);
        });
    }

    @Transactional(readOnly = true)
    public List<CategoryWithMenusResponse> getMenusForCustomer(Long storeId) {
        List<Category> categories = categoryRepository.findAllByStoreIdOrderByDisplayOrder(storeId);
        List<Menu> menus = menuRepository.findAllActiveByStoreId(storeId);
        Map<Long, List<Menu>> menuMap = menus.stream().collect(Collectors.groupingBy(Menu::getCategoryId));

        return categories.stream()
                .map(c -> new CategoryWithMenusResponse(c.getId(), c.getName(), c.getDisplayOrder(),
                        menuMap.getOrDefault(c.getId(), List.of()).stream().map(this::toMenuResponse).toList()))
                .filter(c -> !c.menus().isEmpty())
                .toList();
    }

    private void validatePrice(int price) {
        if (price < 100 || price > 1_000_000) throw new BusinessException(ErrorCode.INVALID_PRICE);
    }

    private CategoryResponse toCategoryResponse(Category c) {
        return new CategoryResponse(c.getId(), c.getStoreId(), c.getName(), c.getDisplayOrder());
    }

    private MenuResponse toMenuResponse(Menu m) {
        return new MenuResponse(m.getId(), m.getStoreId(), m.getCategoryId(), m.getName(),
                m.getPrice(), m.getDescription(), m.getImageUrl(), m.getDisplayOrder(), m.getCreatedAt());
    }
}
