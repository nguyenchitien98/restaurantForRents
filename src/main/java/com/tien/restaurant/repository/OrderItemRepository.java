package com.tien.restaurant.repository;

import com.tien.restaurant.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT m.name FROM OrderItem oi JOIN oi.menu m GROUP BY m.name ORDER BY SUM(oi.quantity) DESC")
    List<String> findTopSellingMenus(Pageable pageable);

    List<OrderItem> findByOrderId(Long orderId);

    default String findBestSellingMenuName() {
        List<String> top = findTopSellingMenus(PageRequest.of(0, 1));
        return top.isEmpty() ? "Chưa có dữ liệu" : top.get(0);
    }

    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}