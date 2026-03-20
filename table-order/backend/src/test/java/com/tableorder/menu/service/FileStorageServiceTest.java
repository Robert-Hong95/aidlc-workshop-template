package com.tableorder.menu.service;

import com.tableorder.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    @TempDir Path tempDir;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService(tempDir.toString());
    }

    @Test
    void uploadFile_정상업로드_JPG() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[100]);

        String url = fileStorageService.uploadFile(file);

        assertThat(url).contains("/api/files/");
    }

    @Test
    void uploadFile_허용되지않는형식_예외() {
        MockMultipartFile file = new MockMultipartFile("file", "test.gif", "image/gif", new byte[100]);

        assertThatThrownBy(() -> fileStorageService.uploadFile(file))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void uploadFile_크기초과_예외() {
        byte[] largeFile = new byte[6 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", largeFile);

        assertThatThrownBy(() -> fileStorageService.uploadFile(file))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void getFile_정상조회() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[100]);
        String url = fileStorageService.uploadFile(file);
        String filename = url.substring(url.lastIndexOf("/") + 1);

        Resource resource = fileStorageService.getFile(filename);

        assertThat(resource.exists()).isTrue();
    }
}
