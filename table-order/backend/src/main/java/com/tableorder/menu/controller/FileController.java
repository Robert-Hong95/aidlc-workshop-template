package com.tableorder.menu.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.menu.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File", description = "파일 업로드/다운로드 API")
@RestController
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) { this.fileStorageService = fileStorageService; }

    @Operation(summary = "이미지 업로드", description = "JPG/PNG만 허용, 최대 5MB")
    @PostMapping("/api/files/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(fileStorageService.uploadFile(file)));
    }

    @Operation(summary = "이미지 다운로드")
    @GetMapping("/api/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource resource = fileStorageService.getFile(filename);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }
}
