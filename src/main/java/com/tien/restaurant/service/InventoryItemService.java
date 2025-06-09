package com.tien.restaurant.service;

import com.tien.restaurant.entity.InventoryItem;
import com.tien.restaurant.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryItemRepository repository;

    public List<InventoryItem> getAll() {
        return repository.findAll();
    }

    public InventoryItem create(InventoryItem item) {
        return repository.save(item);
    }

    public InventoryItem update(Long id, InventoryItem newItem) {
        InventoryItem item = repository.findById(id).orElseThrow();
        item.setName(newItem.getName());
        item.setQuantity(newItem.getQuantity());
        item.setUnit(newItem.getUnit());
        item.setCategory(newItem.getCategory());
        return repository.save(item);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
