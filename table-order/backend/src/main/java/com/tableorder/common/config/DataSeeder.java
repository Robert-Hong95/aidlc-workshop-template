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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class DataSeeder implements ApplicationRunner {

    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;
    private final StoreTableRepository storeTableRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public DataSeeder(StoreRepository storeRepository, AdminRepository adminRepository,
                      StoreTableRepository storeTableRepository, CategoryRepository categoryRepository,
                      MenuRepository menuRepository, PasswordEncoder passwordEncoder) {
        this.storeRepository = storeRepository;
        this.adminRepository = adminRepository;
        this.storeTableRepository = storeTableRepository;
        this.categoryRepository = categoryRepository;
        this.menuRepository = menuRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String seedImage(String filename) {
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            if (!Files.exists(target)) {
                InputStream is = new ClassPathResource("seed-images/" + filename).getInputStream();
                Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
                is.close();
            }
            return "/api/files/" + filename;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        if (storeRepository.count() > 0) return;

        String pw = passwordEncoder.encode("pass1234");
        Store store = storeRepository.save(new Store("STORE01", "테스트매장"));
        Long sid = store.getId();

        adminRepository.save(new Admin(sid, "admin", pw));
        storeTableRepository.save(new StoreTable(sid, 1, pw));
        storeTableRepository.save(new StoreTable(sid, 2, pw));
        storeTableRepository.save(new StoreTable(sid, 3, pw));
        storeTableRepository.save(new StoreTable(sid, 4, pw));

        // 카테고리
        Category cat1 = categoryRepository.save(new Category(sid, "추천메뉴", 1));
        Category cat2 = categoryRepository.save(new Category(sid, "메인요리", 2));
        Category cat3 = categoryRepository.save(new Category(sid, "사이드", 3));
        Category cat4 = categoryRepository.save(new Category(sid, "음료", 4));

        // 추천메뉴
        menuRepository.save(new Menu(sid, cat1.getId(), "시그니처 스테이크", 32000, "부드러운 안심 스테이크와 특제 소스", seedImage("steak.jpg"), 1));
        menuRepository.save(new Menu(sid, cat1.getId(), "트러플 파스타", 18000, "블랙 트러플 오일을 곁들인 크림 파스타", seedImage("truffle-pasta.jpg"), 2));
        menuRepository.save(new Menu(sid, cat1.getId(), "해산물 플래터", 45000, "랍스터, 새우, 조개 모듬", seedImage("seafood-platter.jpg"), 3));

        // 메인요리
        menuRepository.save(new Menu(sid, cat2.getId(), "치킨 스테이크", 22000, "허브 마리네이드 치킨 스테이크", seedImage("chicken-steak.jpg"), 1));
        menuRepository.save(new Menu(sid, cat2.getId(), "연어 스테이크", 28000, "노르웨이산 연어 그릴", seedImage("salmon.jpg"), 2));
        menuRepository.save(new Menu(sid, cat2.getId(), "함박 스테이크", 16000, "수제 함박 스테이크와 데미글라스 소스", seedImage("hamburg-steak.jpg"), 3));
        menuRepository.save(new Menu(sid, cat2.getId(), "봉골레 파스타", 15000, "바지락 봉골레 파스타", seedImage("vongole.jpg"), 4));

        // 사이드
        menuRepository.save(new Menu(sid, cat3.getId(), "시저 샐러드", 12000, "로메인 상추와 파마산 치즈", seedImage("caesar-salad.jpg"), 1));
        menuRepository.save(new Menu(sid, cat3.getId(), "감자튀김", 8000, "바삭한 감자튀김과 트러플 소금", seedImage("fries.jpg"), 2));
        menuRepository.save(new Menu(sid, cat3.getId(), "마늘빵", 6000, "갈릭 버터 바게트", seedImage("garlic-bread.jpg"), 3));
        menuRepository.save(new Menu(sid, cat3.getId(), "콘 수프", 7000, "부드러운 크림 콘 수프", seedImage("corn-soup.jpg"), 4));

        // 음료
        menuRepository.save(new Menu(sid, cat4.getId(), "아메리카노", 5000, "에스프레소 더블샷", seedImage("americano.jpg"), 1));
        menuRepository.save(new Menu(sid, cat4.getId(), "카페라떼", 6000, "부드러운 우유와 에스프레소", seedImage("latte.jpg"), 2));
        menuRepository.save(new Menu(sid, cat4.getId(), "레몬에이드", 7000, "생레몬 에이드", seedImage("lemonade.jpg"), 3));
        menuRepository.save(new Menu(sid, cat4.getId(), "콜라", 3000, "코카콜라 355ml", seedImage("cola.jpg"), 4));
        menuRepository.save(new Menu(sid, cat4.getId(), "맥주", 8000, "생맥주 500ml", seedImage("beer.jpg"), 5));
    }
}
