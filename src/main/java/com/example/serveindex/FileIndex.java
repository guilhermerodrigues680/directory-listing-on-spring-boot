package com.example.serveindex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class FileIndex {

    private String name;
    private LocalDateTime lastModified;
    private long length;

    public String getNameUrlEncode() {
        return UriUtils.encodePath(this.name, StandardCharsets.UTF_8);
    }

}
