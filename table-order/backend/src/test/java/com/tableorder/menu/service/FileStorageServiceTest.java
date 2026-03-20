package com.tableorder.menu.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class FileStorageServiceTest {

    @TempDir Path tempDir;

    @Test @DisplayName("TC-FILE-001: upload 성공")
    void upload_success() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});

        String url = service.upload(file);

        assertThat(url).endsWith(".jpg");
    }

    @Test @DisplayName("TC-FILE-002: upload 실패 - 잘못된 파일 형식")
    void upload_invalidType() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[]{1});

        assertThatThrownBy(() -> service.upload(file))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_FILE_TYPE);
    }
}
