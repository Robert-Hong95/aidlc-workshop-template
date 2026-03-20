package com.tableorder.store.service;

import com.tableorder.auth.domain.Admin;
import com.tableorder.auth.repository.AdminRepository;
import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import com.tableorder.store.domain.Store;
import com.tableorder.store.dto.StoreCreateRequest;
import com.tableorder.store.dto.StoreResponse;
import com.tableorder.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks private StoreService storeService;
    @Mock private StoreRepository storeRepository;
    @Mock private AdminRepository adminRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @Test
    void createStore_정상_매장과관리자생성() {
        var request = new StoreCreateRequest("STORE01", "테스트매장", "admin", "Pass1234!");
        given(storeRepository.existsByStoreCode("STORE01")).willReturn(false);
        given(passwordEncoder.encode("Pass1234!")).willReturn("encoded");
        given(storeRepository.save(any(Store.class))).willAnswer(inv -> inv.getArgument(0));
        given(adminRepository.save(any(Admin.class))).willAnswer(inv -> inv.getArgument(0));

        StoreResponse result = storeService.createStore(request);

        assertThat(result.storeCode()).isEqualTo("STORE01");
        assertThat(result.name()).isEqualTo("테스트매장");
        then(storeRepository).should().save(any(Store.class));
        then(adminRepository).should().save(any(Admin.class));
    }

    @Test
    void createStore_중복코드_예외() {
        var request = new StoreCreateRequest("STORE01", "테스트매장", "admin", "Pass1234!");
        given(storeRepository.existsByStoreCode("STORE01")).willReturn(true);

        assertThatThrownBy(() -> storeService.createStore(request))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.DUPLICATE_STORE_CODE);
    }

    @Test
    void createStore_비밀번호정책위반_예외() {
        var request = new StoreCreateRequest("STORE01", "테스트매장", "admin", "1234");
        given(storeRepository.existsByStoreCode("STORE01")).willReturn(false);

        assertThatThrownBy(() -> storeService.createStore(request))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void getStore_정상조회() {
        Store store = new Store("STORE01", "테스트매장");
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        StoreResponse result = storeService.getStore(1L);

        assertThat(result.storeCode()).isEqualTo("STORE01");
    }

    @Test
    void getStore_없는매장_예외() {
        given(storeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.getStore(999L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.STORE_NOT_FOUND);
    }

    @Test
    void getStores_목록조회() {
        given(storeRepository.findAll()).willReturn(List.of(
                new Store("S1", "매장1"), new Store("S2", "매장2")));

        List<StoreResponse> result = storeService.getStores();

        assertThat(result).hasSize(2);
    }
}
