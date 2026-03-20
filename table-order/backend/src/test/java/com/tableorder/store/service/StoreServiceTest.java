package com.tableorder.store.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.domain.Store;
import com.tableorder.store.dto.*;
import com.tableorder.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks private StoreService storeService;
    @Mock private StoreRepository storeRepository;

    private Store store;

    @BeforeEach
    void setUp() throws Exception {
        store = new Store("STORE01", "테스트매장");
        setId(store, 1L);
    }

    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    @Test @DisplayName("TC-STORE-001: 매장 등록 성공")
    void createStore_success() {
        given(storeRepository.findByStoreCode("NEWSTORE")).willReturn(Optional.empty());
        given(storeRepository.save(any(Store.class))).willAnswer(inv -> {
            Store s = inv.getArgument(0); setId(s, 2L); return s;
        });

        StoreResponse res = storeService.createStore(new StoreCreateRequest("NEWSTORE", "새매장"));
        assertThat(res.storeCode()).isEqualTo("NEWSTORE");
        assertThat(res.name()).isEqualTo("새매장");
    }

    @Test @DisplayName("TC-STORE-002: 중복 매장 코드 등록 실패")
    void createStore_duplicate() {
        given(storeRepository.findByStoreCode("STORE01")).willReturn(Optional.of(store));

        assertThatThrownBy(() -> storeService.createStore(new StoreCreateRequest("STORE01", "매장")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_STORE_CODE.getMessage());
    }

    @Test @DisplayName("TC-STORE-003: 매장 목록 조회")
    void getStores() {
        given(storeRepository.findAll()).willReturn(List.of(store));

        List<StoreResponse> res = storeService.getStores();
        assertThat(res).hasSize(1);
    }

    @Test @DisplayName("TC-STORE-004: 매장 단건 조회 성공")
    void getStore_success() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        StoreResponse res = storeService.getStore(1L);
        assertThat(res.storeCode()).isEqualTo("STORE01");
    }

    @Test @DisplayName("TC-STORE-005: 매장 조회 실패")
    void getStore_notFound() {
        given(storeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.getStore(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.STORE_NOT_FOUND.getMessage());
    }

    @Test @DisplayName("TC-STORE-006: 매장 수정 성공")
    void updateStore_success() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        StoreResponse res = storeService.updateStore(1L, new StoreUpdateRequest("수정매장"));
        assertThat(res.name()).isEqualTo("수정매장");
    }

    @Test @DisplayName("TC-STORE-007: 매장 삭제 성공")
    void deleteStore_success() {
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        assertThatCode(() -> storeService.deleteStore(1L)).doesNotThrowAnyException();
        then(storeRepository).should().delete(store);
    }
}
