package com.tien.restaurant.controller;

import com.tien.restaurant.dto.request.MenuRequest;
import com.tien.restaurant.dto.response.MenuResponse;
import com.tien.restaurant.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuResponse> getAll() {
        return menuService.getAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuResponse> createMenu(
            @RequestPart("data") MenuRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        MenuResponse created = menuService.createMenu(request, image);
        return ResponseEntity.ok(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuResponse> updateMenu(
            @PathVariable Long id,
            @RequestPart("data") MenuRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {

        MenuResponse updated = menuService.updateMenu(id, request, image);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        menuService.delete(id);
    }
}
