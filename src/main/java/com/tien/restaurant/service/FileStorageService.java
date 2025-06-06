package com.tien.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${file.upload-dir}")
    private String fileUploadDir;

    public String uploadImage(MultipartFile file){
        try {
            String uploadDir =  System.getProperty("user.dir") + fileUploadDir;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String filename =file.getOriginalFilename();
            File dest = new File(uploadDir + filename);
            file.transferTo(dest);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            System.out.println("uploadDir: " +uploadDir);
            System.out.println("baseUrl: " +baseUrl);
            LOGGER.info(file.getOriginalFilename());
            return "/uploads/" + filename;
        }catch (IOException e) {
            e.printStackTrace(); // 👈 rất quan trọng
            return "Upload failed: " + e.getMessage();
        }
    }

    public void deleteFile(String imageUrl) {
        String uploadDir =  System.getProperty("user.dir") + fileUploadDir;
        String filename = imageUrl.replace("/uploads/", "");
        File file = new File(uploadDir + filename);
        if (file.exists()) file.delete();
    }

//    public void testWritePermission(String path) {
//        File testFile = new File(path, "test_permission.txt");
//        try (FileWriter writer = new FileWriter(testFile)) {
//            writer.write("Test ghi file thành công!");
//            System.out.println("✅ Ghi file test thành công: " + testFile.getAbsolutePath());
//        } catch (IOException e) {
//            System.err.println("❌ Không ghi được file test: " + e.getMessage());
//        }
//    }
}