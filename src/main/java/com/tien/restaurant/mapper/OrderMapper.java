package com.tien.restaurant.mapper;

import com.tien.restaurant.dto.response.OrderItemResponse;
import com.tien.restaurant.dto.response.OrderResponse;
import com.tien.restaurant.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponse convertToDTO(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setNote(order.getNote());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setOrderType(order.getOrderType());
        dto.setCreatedAt(order.getCreatedAt());

        if (order.getTable() != null) {
            dto.setTable(order.getTable().getTableNumber()); // hoặc getId() tùy bạn
        }

        if (order.getEmployee() != null) {
            dto.setEmployee(order.getEmployee().getName());
        }

        List<OrderItemResponse> itemDTOs = order.getItems().stream().map(item -> {
            OrderItemResponse itemDTO = new OrderItemResponse();
            itemDTO.setId(item.getId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setNote(item.getNote());
            itemDTO.setStatus(item.getStatus());
            itemDTO.setMenuName(item.getMenu().getName()); // <- tên món ăn
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);

        return dto;
    }
}
