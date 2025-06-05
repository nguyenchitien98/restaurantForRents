package com.tien.restaurant.controller;

import com.tien.restaurant.entity.ProductCategory;
import com.tien.restaurant.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService service;

    @GetMapping
    public List<ProductCategory> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ProductCategory create(@RequestBody ProductCategory category) {
        return service.create(category);
    }

    @PutMapping("/{id}")
    public ProductCategory update(@PathVariable Long id, @RequestBody ProductCategory category) {
        return service.update(id, category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}