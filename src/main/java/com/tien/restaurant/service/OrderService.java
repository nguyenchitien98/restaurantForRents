package com.tien.restaurant.service;

import com.tien.restaurant.dto.request.CreateOrderRequest;
import com.tien.restaurant.dto.request.CreateOrderItemRequest;
import com.tien.restaurant.dto.response.GetOrderResponse;
import com.tien.restaurant.entity.*;
import com.tien.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final MenuRepository menuRepo;
    private final OrderItemRepository orderItemRepo;
    private final MenuIngredientRepository menuIngredientRepo;
    private final InventoryItemRepository inventoryItemRepo;
    private final TableRepository tableRepo;
    private final EmployeeRepository employeeRepo;

    public Order cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    public List<GetOrderResponse> getOrdersBetween(LocalDateTime from, LocalDateTime to, List<OrderStatus> statuses) {
        List<Order> orders = orderRepo.findByCreatedAtBetweenAndStatusIn(from, to, statuses);
        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        List<OrderItem> allItems = orderItemRepo.findByOrderIdIn(orderIds);

        Map<Long, Map<String, Integer>> orderIdToItemsMap = new HashMap<>();
        for (OrderItem item : allItems) {
            orderIdToItemsMap
                    .computeIfAbsent(item.getOrder().getId(), k -> new HashMap<>())
                    .merge(item.getMenu().getName(), item.getQuantity(), Integer::sum);
        }

        return orders.stream().map(order -> {
            GetOrderResponse dto = new GetOrderResponse();
            dto.setId(order.getId());
            dto.setStatus(order.getStatus().name());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setNote(null); // Nếu có cột note, gán tại đây
            if (order.getTable() != null) {
                GetOrderResponse.TableDTO tableDTO = new GetOrderResponse.TableDTO();
                tableDTO.setId(order.getTable().getId());
                tableDTO.setTableNumber(order.getTable().getTableNumber());
                dto.setTable(tableDTO);
            }
            dto.setItemsObj(orderIdToItemsMap.getOrDefault(order.getId(), Map.of()));
            return dto;
        }).toList();
    }

    @Transactional
    public Order createOrder(CreateOrderRequest dto) {
        Order order = new Order();
        order.setOrderType(dto.getOrderType());
        order.setCustomerName(dto.getCustomerName());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setNote(dto.getNote());

        if (dto.getTableId() != null) {
            order.setTable(tableRepo.findById(dto.getTableId()).orElse(null));
        }
        if (dto.getEmployeeId() != null) {
            order.setEmployee(employeeRepo.findById(dto.getEmployeeId()).orElse(null));
        }

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemDTO : dto.getItems()) {
            Menu menu = menuRepo.findById(itemDTO.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found"));

            // Trừ kho dựa trên nguyên liệu cần cho món ăn
            List<MenuIngredient> ingredients = menuIngredientRepo.findByMenuId(menu.getId());
            for (MenuIngredient ingredient : ingredients) {
                InventoryItem inventory = ingredient.getInventoryItem();
                BigDecimal used = ingredient.getQuantity().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
                inventory.setQuantity(inventory.getQuantity().subtract(used));
                inventoryItemRepo.save(inventory);
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenu(menu);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setNote(itemDTO.getNote());
            item.setStatus(OrderItemStatus.PENDING);

            items.add(item);
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);

        return orderRepo.save(order);
    }
}
