package com.tien.restaurant.controller;

import com.tien.restaurant.dto.response.MenuIngredientResponse;
import com.tien.restaurant.service.MenuIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu-ingredients")
@RequiredArgsConstructor
public class MenuIngredientController {

    private final MenuIngredientService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MenuIngredientResponse dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MenuIngredientResponse dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/menu/{menuId}")
    public ResponseEntity<?> getByMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(service.getByMenu(menuId));
    }
}
