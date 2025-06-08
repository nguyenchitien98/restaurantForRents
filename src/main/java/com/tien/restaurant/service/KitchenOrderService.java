package com.tien.restaurant.service;

import com.tien.restaurant.dto.response.KitchenOrderResponseDTO;
import com.tien.restaurant.entity.*;
import com.tien.restaurant.repository.KitchenOrderRepository;
import com.tien.restaurant.repository.OrderItemRepository;
import com.tien.restaurant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenOrderService {
     private final KitchenOrderRepository kitchenRepo;
     private final OrderItemRepository itemRepo;
     private final OrderRepository orderRepo;

    public List<KitchenOrderResponseDTO> getAll() {
        return kitchenRepo.findAllKitchenOrdersWithOrderInfo();
    }

    public KitchenOrder updateStatus(Long id, KitchenStatus newStatus) {
        KitchenOrder ko = kitchenRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        ko.setStatus(newStatus);
        kitchenRepo.save(ko);

        // Nếu COMPLETED → cập nhật order_item
        if (newStatus == KitchenStatus.COMPLETED) {
            OrderItem item = itemRepo.findById(ko.getOrderItemId()).orElseThrow();
            item.setStatus(OrderItemStatus.COMPLETED);
            itemRepo.save(item);

            // Check nếu tất cả item của đơn đã completed
            boolean allCompleted = itemRepo
                    .findByOrderId(item.getOrder().getId())
                    .stream().allMatch(i -> i.getStatus() == OrderItemStatus.COMPLETED);

            if (allCompleted) {
                Order order = orderRepo.findById(item.getOrder().getId()).orElseThrow();
                order.setStatus(OrderStatus.COMPLETED);
                orderRepo.save(order);
            }
        }

        return ko;
    }
}
