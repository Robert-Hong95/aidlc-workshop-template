package com.tableorder.menu.service;

import com.tableorder.common.exception.BusinessException;
import com.tableorder.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadDir;
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir);
    }

    public String upload(MultipartFile file) {
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
        try {
            Files.createDirectories(uploadDir);
            String ext = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), uploadDir.resolve(filename));
            return filename;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public Resource loadAsResource(String filename) {
        try {
            Resource resource = new UrlResource(uploadDir.resolve(filename).toUri());
            if (resource.exists()) return resource;
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : "";
    }
}
