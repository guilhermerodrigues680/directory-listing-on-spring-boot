package com.example.serveindex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class FileIndex {

    private String name;
    private LocalDateTime lastModified;
    private long length;

}
