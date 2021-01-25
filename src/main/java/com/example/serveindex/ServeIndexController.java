package com.example.serveindex;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/public")
public class ServeIndexController {

    @GetMapping("**")
    public String indexWithSlash(HttpServletRequest request, HttpServletResponse response, Model model) {
        File publicDirFile = new File("public");
        String uriPath = request.getRequestURI().substring(request.getContextPath().length());
        log.info(uriPath);

        File uriFile = Paths.get(publicDirFile.getPath(), uriPath.replace("/public", "")).toFile();

        // Arquivo solicitado nao existe
        if (!uriFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "notfound404";
        }

        boolean uriIsDir = uriPath.endsWith("/");

        // Solicitou um arquivo e o arquivo existe
        if (!uriIsDir && !uriFile.isDirectory()) {
            try {
                String mimeType = getContentTypeFor(uriFile.getName());
                response.setContentType(mimeType);

                // Arquivos HTML são mostrados no browser, os outros são downloads
                if (mimeType != null && mimeType.equals("text/html")) {
                    response.setHeader("Content-disposition", "inline");
                } else {
                    response.setHeader("Content-disposition", "attachment; filename=" + uriFile.getName());
                }

                InputStream is = new FileInputStream(uriFile);
                // copy it to response's OutputStream
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return null;
        }

        // Caso solicite uma pasta mas é um diretorio, redireciona para o diretorio
        if (!uriIsDir && uriFile.isDirectory()) {
            log.info("Solicitou pasta mas é diretorio");
            return "redirect:" + uriPath + "/";
        }

        // Caso não se encaixe em nenhum do casos entra no novo diretorio
        File currentFilePath = uriFile;

        List<FileIndex> files = Collections.emptyList();
        List<FileIndex> dirs = Collections.emptyList();

        try {
            files = Files.find(Paths.get(currentFilePath.getPath()), 1, (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .map(path -> path.toFile())
                    .map(file1 -> new FileIndex(file1.getName(),
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(file1.lastModified()), ZoneId.systemDefault()),
                            file1.length()))
                    .collect(Collectors.toList());

            dirs = Files.find(Paths.get(currentFilePath.getPath()), 1, (filePath, fileAttr) -> fileAttr.isDirectory())
                    .map(path -> path.toFile())
                    .filter(file -> !file.getName().equals(currentFilePath.getName()))
                    .map(file1 -> new FileIndex(file1.getName(),
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(file1.lastModified()), ZoneId.systemDefault()),
                            file1.length()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("path", uriPath);
        model.addAttribute("dirs", dirs);
        model.addAttribute("files", files);
        return "serveindex";
    }

    private String getContentTypeFor(String filename) {
        //old
        //FileNameMap fileNameMap = URLConnection.getFileNameMap();
        //String mimeType = fileNameMap.getContentTypeFor(uriFile.getName());

        String extension = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse(null);

        return extension == null ? null : MimeMappings.DEFAULT.get(extension);
    }

}
