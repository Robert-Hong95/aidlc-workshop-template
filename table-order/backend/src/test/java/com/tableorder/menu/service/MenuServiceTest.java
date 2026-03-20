package com.tableorder.menu.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.menu.domain.Category;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.dto.*;
import com.tableorder.menu.repository.CategoryRepository;
import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.store.domain.Store;
import com.tableorder.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks private MenuService menuService;
    @Mock private MenuRepository menuRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private StoreRepository storeRepository;

    // --- Category CRUD ---
    @Test
    void createCategory_정상생성() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(categoryRepository.findByStoreIdAndName(1L, "음료")).willReturn(Optional.empty());
        given(categoryRepository.countByStoreId(1L)).willReturn(0);
        given(categoryRepository.save(any(Category.class))).willAnswer(inv -> inv.getArgument(0));

        CategoryResponse result = menuService.createCategory(1L, new CategoryCreateRequest("음료"));

        assertThat(result.name()).isEqualTo("음료");
        assertThat(result.displayOrder()).isEqualTo(1);
    }

    @Test
    void createCategory_중복이름_예외() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(categoryRepository.findByStoreIdAndName(1L, "음료")).willReturn(Optional.of(new Category(1L, "음료", 1)));

        assertThatThrownBy(() -> menuService.createCategory(1L, new CategoryCreateRequest("음료")))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.DUPLICATE_CATEGORY_NAME);
    }

    @Test
    void getCategories_목록조회() {
        given(categoryRepository.findAllByStoreIdOrderByDisplayOrder(1L))
                .willReturn(List.of(new Category(1L, "음료", 1), new Category(1L, "식사", 2)));

        List<CategoryResponse> result = menuService.getCategories(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void updateCategory_정상수정() {
        Category cat = new Category(1L, "음료", 1);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(cat));

        CategoryResponse result = menuService.updateCategory(1L, new CategoryUpdateRequest("커피"));

        assertThat(result.name()).isEqualTo("커피");
    }

    @Test
    void deleteCategory_메뉴있으면_예외() {
        given(categoryRepository.findById(1L)).willReturn(Optional.of(new Category(1L, "음료", 1)));
        given(menuRepository.existsByCategoryId(1L)).willReturn(true);

        assertThatThrownBy(() -> menuService.deleteCategory(1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.CATEGORY_HAS_MENUS);
    }

    @Test
    void deleteCategory_정상삭제() {
        Category cat = new Category(1L, "음료", 1);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(cat));
        given(menuRepository.existsByCategoryId(1L)).willReturn(false);

        menuService.deleteCategory(1L);

        then(categoryRepository).should().delete(cat);
    }

    // --- Menu CRUD ---
    @Test
    void createMenu_정상생성() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(categoryRepository.findById(10L)).willReturn(Optional.of(new Category(1L, "음료", 1)));
        given(menuRepository.countByStoreId(1L)).willReturn(0);
        given(menuRepository.save(any(Menu.class))).willAnswer(inv -> inv.getArgument(0));

        MenuResponse result = menuService.createMenu(1L, new MenuCreateRequest("아메리카노", 4500, "설명", 10L, null));

        assertThat(result.name()).isEqualTo("아메리카노");
        assertThat(result.price()).isEqualTo(4500);
    }

    @Test
    void createMenu_카테고리없음_예외() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(new Store("S1", "매장")));
        given(categoryRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.createMenu(1L, new MenuCreateRequest("아메리카노", 4500, null, 999L, null)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void updateMenu_정상수정() {
        Menu menu = new Menu(1L, 10L, "아메리카노", 4500, null, null, 1);
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        MenuResponse result = menuService.updateMenu(1L, new MenuUpdateRequest("라떼", 5000, null, null, null));

        assertThat(result.name()).isEqualTo("라떼");
        assertThat(result.price()).isEqualTo(5000);
    }

    @Test
    void deleteMenu_softDelete() {
        Menu menu = new Menu(1L, 10L, "아메리카노", 4500, null, null, 1);
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        menuService.deleteMenu(1L);

        assertThat(menu.isAvailable()).isFalse();
    }

    @Test
    void getAllMenus_관리자용_전체조회() {
        given(menuRepository.findAllByStoreIdOrderByDisplayOrder(1L))
                .willReturn(List.of(new Menu(1L, 10L, "아메리카노", 4500, null, null, 1)));

        List<MenuResponse> result = menuService.getAllMenus(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void updateMenuOrder_일괄변경() throws Exception {
        Menu m1 = new Menu(1L, 10L, "아메리카노", 4500, null, null, 1);
        Menu m2 = new Menu(1L, 10L, "라떼", 5000, null, null, 2);
        setId(m1, 1L);
        setId(m2, 2L);
        given(menuRepository.findAllById(List.of(1L, 2L))).willReturn(List.of(m1, m2));

        menuService.updateMenuOrder(1L, List.of(new MenuOrderItem(1L, 2), new MenuOrderItem(2L, 1)));

        assertThat(m1.getDisplayOrder()).isEqualTo(2);
        assertThat(m2.getDisplayOrder()).isEqualTo(1);
    }

    private void setId(Object entity, Long id) throws Exception {
        var field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    // --- Customer ---
    @Test
    void getMenusForCustomer_카테고리별메뉴() {
        Category cat = new Category(1L, "음료", 1);
        given(categoryRepository.findAllByStoreIdOrderByDisplayOrder(1L)).willReturn(List.of(cat));
        given(menuRepository.findAllByCategoryIdAndAvailableTrueOrderByDisplayOrder(any()))
                .willReturn(List.of(new Menu(1L, cat.getId(), "아메리카노", 4500, null, null, 1)));

        List<CategoryWithMenusResponse> result = menuService.getMenusForCustomer(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).menus()).hasSize(1);
    }
}
