package com.tien.restaurant.repository;

import com.tien.restaurant.dto.response.KitchenOrderResponseDTO;
import com.tien.restaurant.entity.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {

    @Query("""
    SELECT new com.tien.restaurant.dto.response.KitchenOrderResponseDTO(
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
    List<KitchenOrderResponseDTO> findAllKitchenOrdersWithOrderInfo();
}