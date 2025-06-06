package com.tien.restaurant.service;

import com.tien.restaurant.dto.request.MenuRequest;
import com.tien.restaurant.dto.response.MenuResponse;
import com.tien.restaurant.entity.Menu;
import com.tien.restaurant.entity.ProductCategory;
import com.tien.restaurant.repository.MenuRepository;
import com.tien.restaurant.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RestController
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductCategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public MenuResponse createMenu(MenuRequest request, MultipartFile image) {
        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setDescription(request.getDescription());
        menu.setPrice(request.getPrice());
        menu.setIsAvailable(request.getIsAvailable());
//        fileStorageService.testWritePermission("D:/ProjectJava/restaurant/uploads/");
        String imageUrl = fileStorageService.uploadImage(image);
        System.out.println(imageUrl);
        menu.setImage(imageUrl);
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    public MenuResponse updateMenu(Long id, MenuRequest request, MultipartFile image) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        // Xóa ảnh cũ nếu có ảnh mới
        if (image != null && menu.getImage() != null) {
            fileStorageService.deleteFile(menu.getImage());
        }

        updateFields(menu, request, image);
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    private void updateFields(Menu menu, MenuRequest request, MultipartFile image) {
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        menu.setDescription(request.getDescription());
        menu.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);

        if (request.getCategoryId() != null) {
            ProductCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            menu.setCategory(category);
        }

        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.uploadImage(image);
            menu.setImage(imagePath);
        }
    }

    public void delete(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow();
        if (menu.getImage() != null) {
            fileStorageService.deleteFile(menu.getImage());
        }
        menuRepository.deleteById(id);
    }

    public List<MenuResponse> getAll() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
