package com.tien.restaurant.repository;

import com.tien.restaurant.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoff")
    void deleteOlderThan(@Param("cutoff") LocalDateTime cutoff);
}
