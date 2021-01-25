package com.example.serveindex.controller.api;

import com.example.serveindex.exception.UploadFileException;
import com.example.serveindex.model.UploadFileResponse;
import com.example.serveindex.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadFileController {

    @Autowired
    UploadFileService uploadFileService;

    @PostMapping
    public ResponseEntity<UploadFileResponse> uploadData(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você deve selecionar um arquivo para enviar");
        }

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você deve selecionar um arquivo não vazio para enviar");
        }

        try {
            return ResponseEntity.ok(uploadFileService.uploadFile(file));
        } catch (UploadFileException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
