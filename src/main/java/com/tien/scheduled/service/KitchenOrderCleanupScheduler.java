package com.tien.scheduled.service;

import com.tien.restaurant.repository.KitchenOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KitchenOrderCleanupScheduler {

    private final KitchenOrderRepository kitchenOrderRepository;

    // Chạy lúc 3h sáng hàng ngày
    @Scheduled(cron = "0 0 3 * * *") // Giờ hệ thống (ví dụ GMT+7 thì đúng 3h)
    public void deleteOldKitchenOrders() {
        int deletedCount = kitchenOrderRepository.deleteAllBeforeToday();
        log.info("✅ KitchenOrder cleanup done: {} records deleted", deletedCount);
    }
}
