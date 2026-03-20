package com.tableorder.menu.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.menu.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileStorageService fileStorageService;
    public FileController(FileStorageService fileStorageService) { this.fileStorageService = fileStorageService; }

    @PostMapping("/upload")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) {
        return ApiResponse.ok(fileStorageService.upload(file));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        return ResponseEntity.ok(fileStorageService.loadAsResource(filename));
    }
}
