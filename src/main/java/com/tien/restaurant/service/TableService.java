package com.tien.restaurant.service;

import com.tien.restaurant.entity.TableEntity;
import com.tien.restaurant.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;

    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    public TableEntity createTable(TableEntity table) {
        return tableRepository.save(table);
    }

    public TableEntity updateTable(Long id, TableEntity updatedTable) {
        return tableRepository.findById(id).map(table -> {
            table.setTableNumber(updatedTable.getTableNumber());
            table.setCapacity(updatedTable.getCapacity());
            table.setStatus(updatedTable.getStatus());
            return tableRepository.save(table);
        }).orElseThrow(() -> new RuntimeException("Table not found with id " + id));
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}