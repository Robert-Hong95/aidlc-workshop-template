package com.tableorder.common.config;

import com.tableorder.auth.domain.Admin;
import com.tableorder.auth.repository.AdminRepository;
import com.tableorder.menu.domain.Category;
import com.tableorder.menu.domain.Menu;
import com.tableorder.menu.repository.CategoryRepository;
import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.store.domain.Store;
import com.tableorder.store.domain.StoreTable;
import com.tableorder.store.repository.StoreRepository;
import com.tableorder.store.repository.StoreTableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("local")
public class DataInitializer {

    @Bean
    CommandLineRunner initData(StoreRepository storeRepo, AdminRepository adminRepo,
                               StoreTableRepository tableRepo, CategoryRepository categoryRepo,
                               MenuRepository menuRepo, PasswordEncoder encoder) {
        return args -> {
            // 매장
            Store store = storeRepo.save(new Store("STORE001", "위메이드 카페"));
            Long sid = store.getId();

            // 관리자 (admin / password123)
            adminRepo.save(new Admin(sid, "admin", encoder.encode("password123")));

            // 테이블 3개 (비밀번호: 1234)
            String tablePw = encoder.encode("1234");
            tableRepo.save(new StoreTable(sid, 1, tablePw));
            tableRepo.save(new StoreTable(sid, 2, tablePw));
            tableRepo.save(new StoreTable(sid, 3, tablePw));

            // 카테고리
            Category coffee = categoryRepo.save(new Category(sid, "커피", 1));
            Category nonCoffee = categoryRepo.save(new Category(sid, "논커피", 2));
            Category dessert = categoryRepo.save(new Category(sid, "디저트", 3));

            // 메뉴
            menuRepo.save(new Menu(sid, coffee.getId(), "아메리카노", 4500, "깊은 풍미의 에스프레소", null, 1));
            menuRepo.save(new Menu(sid, coffee.getId(), "카페라떼", 5000, "부드러운 우유와 에스프레소", null, 2));
            menuRepo.save(new Menu(sid, coffee.getId(), "카푸치노", 5000, "풍성한 우유 거품", null, 3));
            menuRepo.save(new Menu(sid, nonCoffee.getId(), "녹차라떼", 5500, "진한 말차와 우유", null, 1));
            menuRepo.save(new Menu(sid, nonCoffee.getId(), "초코라떼", 5500, "달콤한 초콜릿", null, 2));
            menuRepo.save(new Menu(sid, dessert.getId(), "치즈케이크", 6500, "뉴욕 스타일 치즈케이크", null, 1));
            menuRepo.save(new Menu(sid, dessert.getId(), "티라미수", 7000, "이탈리안 클래식 디저트", null, 2));

            System.out.println("=== 초기 데이터 로딩 완료 ===");
            System.out.println("매장: STORE001 (위메이드 카페)");
            System.out.println("관리자: admin / password123");
            System.out.println("테이블: 1~3번 (비밀번호: 1234)");
            System.out.println("메뉴: 커피 3개, 논커피 2개, 디저트 2개");
        };
    }
}
