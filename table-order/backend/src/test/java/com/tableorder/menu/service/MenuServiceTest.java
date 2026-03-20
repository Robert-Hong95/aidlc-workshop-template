package com.tableorder.menu.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.domain.Category;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.dto.*;
import com.tableorder.menu.repository.CategoryRepository;
import com.tableorder.menu.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock private MenuRepository menuRepository;
    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private MenuService menuService;

    private <T> void setId(T entity, Long id) throws Exception {
        Field f = entity.getClass().getDeclaredField("id");
        f.setAccessible(true);
        f.set(entity, id);
    }

    // === Category Tests ===

    @Test @DisplayName("TC-MENU-001: createCategory 성공")
    void createCategory_success() throws Exception {
        Category saved = new Category(1L, "음료", 0);
        setId(saved, 1L);
        given(categoryRepository.save(any())).willReturn(saved);

        CategoryResponse result = menuService.createCategory(1L, new CategoryCreateRequest("음료"));

        assertThat(result.name()).isEqualTo("음료");
        assertThat(result.storeId()).isEqualTo(1L);
    }

    @Test @DisplayName("TC-MENU-002: getCategories 성공")
    void getCategories_success() throws Exception {
        Category c1 = new Category(1L, "음료", 0); setId(c1, 1L);
        Category c2 = new Category(1L, "식사", 1); setId(c2, 2L);
        given(categoryRepository.findAllByStoreIdOrderByDisplayOrder(1L)).willReturn(List.of(c1, c2));

        List<CategoryResponse> result = menuService.getCategories(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("음료");
    }

    @Test @DisplayName("TC-MENU-003: updateCategory 성공")
    void updateCategory_success() throws Exception {
        Category cat = new Category(1L, "음료", 0); setId(cat, 1L);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(cat));

        CategoryResponse result = menuService.updateCategory(1L, new CategoryCreateRequest("커피"));

        assertThat(result.name()).isEqualTo("커피");
    }

    @Test @DisplayName("TC-MENU-004: deleteCategory 성공")
    void deleteCategory_success() throws Exception {
        Category cat = new Category(1L, "음료", 0); setId(cat, 1L);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(cat));
        given(menuRepository.existsByCategoryIdAndDeletedFalse(1L)).willReturn(false);

        menuService.deleteCategory(1L);

        verify(categoryRepository).delete(cat);
    }

    @Test @DisplayName("TC-MENU-005: deleteCategory 실패 - 메뉴 존재")
    void deleteCategory_hasMenus() throws Exception {
        Category cat = new Category(1L, "음료", 0); setId(cat, 1L);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(cat));
        given(menuRepository.existsByCategoryIdAndDeletedFalse(1L)).willReturn(true);

        assertThatThrownBy(() -> menuService.deleteCategory(1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.CATEGORY_HAS_MENUS);
    }

    @Test @DisplayName("TC-MENU-006: updateCategoryOrder 성공")
    void updateCategoryOrder_success() throws Exception {
        Category c1 = new Category(1L, "음료", 0); setId(c1, 1L);
        Category c2 = new Category(1L, "식사", 1); setId(c2, 2L);
        given(categoryRepository.findAllById(List.of(2L, 1L))).willReturn(List.of(c2, c1));

        menuService.updateCategoryOrder(1L, new DisplayOrderRequest(List.of(2L, 1L)));

        assertThat(c2.getDisplayOrder()).isEqualTo(0);
        assertThat(c1.getDisplayOrder()).isEqualTo(1);
    }

    // === Menu Tests ===

    @Test @DisplayName("TC-MENU-007: createMenu 성공")
    void createMenu_success() throws Exception {
        given(categoryRepository.existsById(1L)).willReturn(true);
        Menu saved = new Menu(1L, 1L, "아메리카노", 4500, "설명", null, 0);
        setId(saved, 1L);
        given(menuRepository.save(any())).willReturn(saved);

        MenuResponse result = menuService.createMenu(1L, new MenuCreateRequest(1L, "아메리카노", 4500, "설명", null));

        assertThat(result.name()).isEqualTo("아메리카노");
        assertThat(result.price()).isEqualTo(4500);
    }

    @Test @DisplayName("TC-MENU-008: createMenu 실패 - 가격 범위 초과")
    void createMenu_invalidPrice() {
        given(categoryRepository.existsById(1L)).willReturn(true);

        assertThatThrownBy(() -> menuService.createMenu(1L, new MenuCreateRequest(1L, "메뉴", 0, null, null)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_PRICE);
    }

    @Test @DisplayName("TC-MENU-009: getMenus 성공")
    void getMenus_success() throws Exception {
        Menu m = new Menu(1L, 1L, "아메리카노", 4500, null, null, 0); setId(m, 1L);
        given(menuRepository.findAllActiveByStoreId(1L)).willReturn(List.of(m));

        List<MenuResponse> result = menuService.getMenus(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("아메리카노");
    }

    @Test @DisplayName("TC-MENU-010: updateMenu 성공")
    void updateMenu_success() throws Exception {
        Menu m = new Menu(1L, 1L, "아메리카노", 4500, null, null, 0); setId(m, 1L);
        given(menuRepository.findById(1L)).willReturn(Optional.of(m));

        MenuResponse result = menuService.updateMenu(1L, new MenuUpdateRequest("라떼", 5000, "우유", null));

        assertThat(result.name()).isEqualTo("라떼");
        assertThat(result.price()).isEqualTo(5000);
    }

    @Test @DisplayName("TC-MENU-011: deleteMenu soft delete")
    void deleteMenu_success() throws Exception {
        Menu m = new Menu(1L, 1L, "아메리카노", 4500, null, null, 0); setId(m, 1L);
        given(menuRepository.findById(1L)).willReturn(Optional.of(m));

        menuService.deleteMenu(1L);

        assertThat(m.isDeleted()).isTrue();
    }

    @Test @DisplayName("TC-MENU-012: updateMenuOrder 성공")
    void updateMenuOrder_success() throws Exception {
        Menu m1 = new Menu(1L, 1L, "A", 1000, null, null, 0); setId(m1, 1L);
        Menu m2 = new Menu(1L, 1L, "B", 2000, null, null, 1); setId(m2, 2L);
        given(menuRepository.findAllById(List.of(2L, 1L))).willReturn(List.of(m2, m1));

        menuService.updateMenuOrder(1L, new DisplayOrderRequest(List.of(2L, 1L)));

        assertThat(m2.getDisplayOrder()).isEqualTo(0);
        assertThat(m1.getDisplayOrder()).isEqualTo(1);
    }

    @Test @DisplayName("TC-MENU-013: getMenusForCustomer 성공")
    void getMenusForCustomer_success() throws Exception {
        Category c = new Category(1L, "음료", 0); setId(c, 1L);
        given(categoryRepository.findAllByStoreIdOrderByDisplayOrder(1L)).willReturn(List.of(c));
        Menu m = new Menu(1L, 1L, "아메리카노", 4500, null, null, 0); setId(m, 1L);
        given(menuRepository.findAllActiveByStoreId(1L)).willReturn(List.of(m));

        List<CategoryWithMenusResponse> result = menuService.getMenusForCustomer(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).categoryName()).isEqualTo("음료");
        assertThat(result.get(0).menus()).hasSize(1);
    }

    @Test @DisplayName("TC-MENU-014: getMenusForCustomer 빈 결과")
    void getMenusForCustomer_empty() {
        given(categoryRepository.findAllByStoreIdOrderByDisplayOrder(1L)).willReturn(List.of());
        given(menuRepository.findAllActiveByStoreId(1L)).willReturn(List.of());

        List<CategoryWithMenusResponse> result = menuService.getMenusForCustomer(1L);

        assertThat(result).isEmpty();
    }

    @Test @DisplayName("TC-MENU-015: createMenu 실패 - 카테고리 없음")
    void createMenu_categoryNotFound() {
        given(categoryRepository.existsById(99L)).willReturn(false);

        assertThatThrownBy(() -> menuService.createMenu(1L, new MenuCreateRequest(99L, "메뉴", 5000, null, null)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
