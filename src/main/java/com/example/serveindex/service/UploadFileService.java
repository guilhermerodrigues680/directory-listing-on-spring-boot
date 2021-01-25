package com.example.serveindex.service;

import com.example.serveindex.exception.UploadFileException;
import com.example.serveindex.model.UploadFileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class UploadFileService {

    public String uploadDir = "public";

    public UploadFileResponse uploadFile(MultipartFile file) throws UploadFileException {
        try {
            //InputStream inputStream = file.getInputStream();
            String originalName = file.getOriginalFilename();
            String name = file.getName();
            String contentType = file.getContentType();
            long size = file.getSize();

            log.info("Upload File -> originalName: {} , name: {} , contentType: {} , size: {}", originalName, name, contentType, size);

            Path copyLocation = Paths.get(uploadDir, StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return new UploadFileResponse(copyLocation.getFileName().toString(), "/" + copyLocation.toString(), contentType, size);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new UploadFileException("Erro ao fazer uploado do arquivo", ioException);
        }
    }

}
