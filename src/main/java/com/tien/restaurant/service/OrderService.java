package com.tien.restaurant.service;

import com.tien.restaurant.dto.response.OrderResponseDTO;
import com.tien.restaurant.entity.Order;
import com.tien.restaurant.entity.OrderItem;
import com.tien.restaurant.entity.OrderStatus;
import com.tien.restaurant.repository.OrderItemRepository;
import com.tien.restaurant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public List<OrderResponseDTO> getOrdersBetween(LocalDateTime from, LocalDateTime to, List<OrderStatus> statuses) {
        List<Order> orders = orderRepository.findByCreatedAtBetweenAndStatusIn(from, to, statuses);
        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        List<OrderItem> allItems = orderItemRepository.findByOrderIdIn(orderIds);

        Map<Long, Map<String, Integer>> orderIdToItemsMap = new HashMap<>();
        for (OrderItem item : allItems) {
            orderIdToItemsMap
                    .computeIfAbsent(item.getOrder().getId(), k -> new HashMap<>())
                    .merge(item.getMenu().getName(), item.getQuantity(), Integer::sum);
        }

        return orders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(order.getId());
            dto.setStatus(order.getStatus().name());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setNote(null); // Nếu có cột note, gán tại đây
            if (order.getTable() != null) {
                OrderResponseDTO.TableDTO tableDTO = new OrderResponseDTO.TableDTO();
                tableDTO.setId(order.getTable().getId());
                tableDTO.setTableNumber(order.getTable().getTableNumber());
                dto.setTable(tableDTO);
            }
            dto.setItemsObj(orderIdToItemsMap.getOrDefault(order.getId(), Map.of()));
            return dto;
        }).toList();
    }
}
