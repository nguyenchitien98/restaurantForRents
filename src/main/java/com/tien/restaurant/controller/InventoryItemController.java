package com.tien.restaurant.controller;

import com.tien.restaurant.entity.InventoryItem;
import com.tien.restaurant.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService service;

    @GetMapping
    public List<InventoryItem> getAll() {
        return service.getAll();
    }

    @PostMapping
    public InventoryItem create(@RequestBody InventoryItem item) {
        return service.create(item);
    }

    @PutMapping("/{id}")
    public InventoryItem update(@PathVariable Long id, @RequestBody InventoryItem item) {
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
