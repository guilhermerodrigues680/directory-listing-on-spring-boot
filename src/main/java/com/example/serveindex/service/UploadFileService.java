package com.example.serveindex.service;

import com.example.serveindex.exception.UploadFileException;
import com.example.serveindex.model.UploadFileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UploadFileService {

    public String uploadDir = "public";

    public UploadFileResponse uploadFile(MultipartFile file) throws UploadFileException {
//        if (file == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você deve selecionar um arquivo para enviar");
//        }
//
//        if (file.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você deve selecionar um arquivo não vazio para enviar");
//        }

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

    public List<UploadFileResponse> uploadMultipleFiles(MultipartFile[] files) throws UploadFileException {
//        if (file == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você deve selecionar um arquivo para enviar");
//        }
//
//        if (file.length == 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você deve selecionar um arquivo não vazio para enviar");
//        }

        List<UploadFileResponse> uploadFileResponseList = new ArrayList<>();

        try {
            for (MultipartFile fileClient : files) {

                String originalName = fileClient.getOriginalFilename();
                String name = fileClient.getName();
                String contentType = fileClient.getContentType();
                long size = fileClient.getSize();

                if (fileClient.getOriginalFilename().toLowerCase().contains(".DS_Store".toLowerCase())) {
                    log.info("Ignorando File -> originalName: {} , name: {} , contentType: {} , size: {}", originalName, name, contentType, humanReadableByteCount(size, true));
                    continue;
                }

                log.info("Upload File -> originalName: {} , name: {} , contentType: {} , size: {}", originalName, name, contentType, humanReadableByteCount(size, true));
                String cleanPath = StringUtils.cleanPath(fileClient.getOriginalFilename());
                Path copyLocation = Paths.get(uploadDir, cleanPath);
                File fileClientDir = copyLocation.getParent().toFile();

                if (!fileClientDir.exists()) {
                    log.info("Os diretorios: '{}' não existem, criando...", fileClientDir.toString());

                    if (!fileClientDir.mkdirs()) {
                        throw new UploadFileException(String.format("Erro interno. Não foi possivel criar o diretorio: %s", fileClientDir.toString()));
                    }

                    log.info("Criado com sucesso!");
                }

                InputStream inputStream = fileClient.getInputStream();
                Files.copy(inputStream, copyLocation, StandardCopyOption.REPLACE_EXISTING);
                uploadFileResponseList.add(new UploadFileResponse(copyLocation.getFileName().toString(), "/" + copyLocation.toString(), contentType, size));
            }

            return uploadFileResponseList;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new UploadFileException("Erro ao fazer upload do arquivo", ioException);
        }
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
