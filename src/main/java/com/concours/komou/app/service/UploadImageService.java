package com.concours.komou.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

@Service
public class UploadImageService {
    public String uploadImage(MultipartFile photo, String link) throws IOException {
        Path path = Paths.get(link);
        Calendar cal = Calendar.getInstance();
        Long millisDate = cal.getTimeInMillis();
        InputStream inputStream = photo.getInputStream();
        Files.copy(inputStream, path.resolve(millisDate + photo.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        return millisDate.toString() + photo.getOriginalFilename();
    }

    public String updateImage(MultipartFile photo, String link, String fileName) throws IOException {
        Path path = Paths.get(link);
        InputStream inputStream = photo.getInputStream();
        Files.copy(inputStream, path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
