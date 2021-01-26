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

    public String getHumanReadableByteCount(boolean si) {
        long bytes = this.length;
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
