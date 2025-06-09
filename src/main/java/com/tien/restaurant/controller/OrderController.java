package com.tien.restaurant.controller;
import com.tien.restaurant.dto.request.CreateOrderRequest;
import com.tien.restaurant.dto.response.OrderResponse;
import com.tien.restaurant.dto.response.GetOrderResponse;
import com.tien.restaurant.entity.Order;
import com.tien.restaurant.entity.OrderStatus;
import com.tien.restaurant.mapper.OrderMapper;
import com.tien.restaurant.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest dto) {
        Order order = orderService.createOrder(dto);
        OrderResponse responseDTO = OrderMapper.convertToDTO(order);
        return ResponseEntity.ok(responseDTO);
    }

    // Lấy đơn theo khoảng thời gian và trạng thái
    @GetMapping
    public ResponseEntity<List<GetOrderResponse>> getOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam List<OrderStatus> statuses
    ) {
        return ResponseEntity.ok(orderService.getOrdersBetween(from, to, statuses));
    }

    // Hủy đơn hàng
    @PutMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}