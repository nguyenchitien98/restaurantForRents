package com.tien.restaurant.service;

import com.tien.restaurant.dto.DailyOrderStat;
import com.tien.restaurant.dto.DashboardStat;
import com.tien.restaurant.repository.OrderItemRepository;
import com.tien.restaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public DashboardService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<DashboardStat> getDashboardSummary() {
        long todayOrders = orderRepository.countOrdersToday();
        BigDecimal todayRevenue = orderRepository.sumRevenueToday();
        String bestSelling = orderItemRepository.findBestSellingMenuName();
        String kitchenStatus = "3/4 bếp"; // hardcoded for now

        return List.of(
                new DashboardStat("Đơn hàng hôm nay", String.valueOf(todayOrders), "shopping"),
                new DashboardStat("Doanh thu hôm nay", todayRevenue + "đ", "money"),
                new DashboardStat("Món bán chạy", bestSelling, "coffee"),
                new DashboardStat("Bếp đang hoạt động", kitchenStatus, "flame")
        );
    }

    public List<DailyOrderStat> getOrdersPerDay() {
        return orderRepository.getOrdersPerLast7Days();
    }
}