package com.tien.restaurant.repository;

import com.tien.restaurant.dto.DailyOrderStat;
import com.tien.restaurant.entity.Order;
import com.tien.restaurant.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COUNT(o) FROM Order o WHERE DATE(o.createdAt) = CURRENT_DATE")
    long countOrdersToday();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE DATE(o.createdAt) = CURRENT_DATE")
    BigDecimal sumRevenueToday();

    @Query(value = "SELECT DATE(created_at) as date, COUNT(*) as count " +
            "FROM orders " +
            "WHERE created_at >= CURDATE() - INTERVAL 7 DAY " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at)", nativeQuery = true)
    List<DailyOrderStat> getOrdersPerLast7Days();

    // Lấy đơn theo khoảng thời gian và trạng thái
    List<Order> findByCreatedAtBetweenAndStatusIn(LocalDateTime from, LocalDateTime to, List<OrderStatus> statuses);
}
