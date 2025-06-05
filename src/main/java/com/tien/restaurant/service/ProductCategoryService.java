package com.tien.restaurant.service;

import com.tien.restaurant.entity.ProductCategory;
import com.tien.restaurant.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository repository;

    public List<ProductCategory> getAll() {
        return repository.findAll();
    }

    public ProductCategory create(ProductCategory category) {
        return repository.save(category);
    }

    public ProductCategory update(Long id, ProductCategory updated) {
        ProductCategory existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        existing.setName(updated.getName());
        existing.setIcon(updated.getIcon());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        repository.deleteById(id);
    }
}