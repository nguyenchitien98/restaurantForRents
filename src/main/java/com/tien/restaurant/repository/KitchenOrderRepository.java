package com.tien.restaurant.repository;

import com.tien.restaurant.dto.response.KitchenOrderResponse;
import com.tien.restaurant.entity.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {

    @Query("""
    SELECT new com.tien.restaurant.dto.response.KitchenOrderResponse(
        ko.id,
        ko.orderId,
        ko.orderItemId,
        ko.itemName,
        ko.quantity,
        ko.status,
        o.createdAt,
        o.table.id
    )
    FROM KitchenOrder ko
    JOIN Order o ON ko.orderId = o.id
""")
    List<KitchenOrderResponse> findAllKitchenOrdersWithOrderInfo();

    @Transactional
    @Modifying
    @Query("DELETE FROM KitchenOrder ko WHERE ko.createdAt < :cutoff")
    int deleteAllBefore(LocalDateTime cutoff);

    default int deleteAllBeforeToday() {
        // 00:00 hôm nay
        LocalDateTime cutoff = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return deleteAllBefore(cutoff);
    }

}