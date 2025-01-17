package com.erp.techInovate.techInovate.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class FileController {

    private final String externalPath = "/Users/heojunseo/Desktop/HeoJunseo/Spring/techInovatePicture/";

    @GetMapping(value = "/files/{filename:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> serveFile(@PathVariable String filename) {
        File file = new File(externalPath + filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build(); // 파일이 존재하지 않을 경우 404 반환
        }
        return ResponseEntity.ok(new FileSystemResource(file)); // 파일이 존재할 경우 반환
    }

}
