package com.tien.restaurant.controller;

import com.tien.restaurant.dto.response.KitchenOrderResponse;
import com.tien.restaurant.entity.KitchenOrder;
import com.tien.restaurant.entity.KitchenStatus;
import com.tien.restaurant.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kitchen-orders")
@RequiredArgsConstructor
public class KitchenOrderController {
     private final KitchenOrderService kitchenService;

    @GetMapping
    public List<KitchenOrderResponse> getAll() {
        return kitchenService.getAll();
    }

    @PutMapping("/{id}")
    public KitchenOrder updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        KitchenStatus status = KitchenStatus.valueOf(payload.get("status"));
        return kitchenService.updateStatus(id, status);
    }
}