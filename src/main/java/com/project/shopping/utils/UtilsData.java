package com.project.shopping.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UtilsData {
    @Deprecated
    public static byte[] getBlobToByte(Blob blob) {
        byte[] bytes = {};

        try {
            bytes = blob.getBinaryStream().readAllBytes();
        } catch (SQLException e) {
            log.error("sql exception {}", e.getMessage());
        } catch (IOException e2) {
            log.error("input output exception {}", e2.getMessage());
        }

        return bytes;
    }

    public static final String getFileBasePath() {
        final String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhh"));
        final String basePath = "d:\\devImages".concat(File.separator).concat(datePath);

        return basePath;
    }

    public static final boolean fileUpload(MultipartFile file, String path) {
        boolean result = true;

        try {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                File directory = new File(path);
                File image = new File(path.concat(File.separator).concat(fileName));

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                file.transferTo(image);
            }
        } catch (Exception e) {
            log.error("file upload error : {}", e.getMessage());
            result = false;
        }

        return result;
    }
}
