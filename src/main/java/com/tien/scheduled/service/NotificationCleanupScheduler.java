package com.tien.scheduled.service;

import com.tien.restaurant.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationCleanupScheduler {
    private final NotificationRepository notificationRepository;

    public NotificationCleanupScheduler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?") // Mỗi ngày lúc 3:00 sáng
    public void deleteOldNotifications() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(15);
        notificationRepository.deleteOlderThan(cutoff);
    }
}
