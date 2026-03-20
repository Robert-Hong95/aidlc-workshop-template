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

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    private final Path uploadPath;

    public FileStorageService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try { Files.createDirectories(this.uploadPath); } catch (IOException e) { throw new RuntimeException(e); }
    }

    public String uploadFile(MultipartFile file) {
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        try {
            String ext = file.getOriginalFilename() != null ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), uploadPath.resolve(filename));
            return "/api/files/" + filename;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public Resource getFile(String filename) {
        try {
            Path file = uploadPath.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
            return resource;
        } catch (Exception e) {
            if (e instanceof BusinessException) throw (BusinessException) e;
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }
}
