package com.tien.restaurant.controller;

import com.tien.restaurant.entity.TableEntity;
import com.tien.restaurant.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;

    @GetMapping
    public List<TableEntity> getAll() {
        return tableService.getAllTables();
    }

    @PostMapping
    public TableEntity create(@RequestBody TableEntity table) {
        return tableService.createTable(table);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableEntity> updateTable(
            @PathVariable Long id,
            @RequestBody TableEntity updatedTable) {
        try {
            TableEntity table = tableService.updateTable(id, updatedTable);
            return ResponseEntity.ok(table);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}