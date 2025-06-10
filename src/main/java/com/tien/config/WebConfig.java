package com.tien.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Value("${upload-dir}")
//    private String uploadPath;
    // Cho phép truy cập ảnh
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy path thư mục gốc đang chạy
        // Cách 1: Cách phổ thông là dùng replace để loại bỏ hai dấu \\ thành dấu /
//        String uploadPath = System.getProperty("user.dir") + "/uploads/";
//        uploadPath = uploadPath.replace("\\", "/");
//        System.out.println(uploadPath);

        // Cách 2: Dùng Paths và toUri()
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads/");
        System.out.println("Upload path: " + uploadPath);
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}