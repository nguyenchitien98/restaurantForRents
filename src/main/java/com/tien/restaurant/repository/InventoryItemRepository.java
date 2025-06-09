package com.tien.restaurant.repository;

import com.tien.restaurant.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {}