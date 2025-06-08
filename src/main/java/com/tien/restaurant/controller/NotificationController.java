package com.tien.restaurant.controller;

import com.tien.restaurant.dto.request.CreateNotificationRequest;
import com.tien.restaurant.dto.response.NotificationResponse;
import com.tien.restaurant.entity.Notification;
import com.tien.restaurant.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getAll() {
        return notificationService.getAll();
    }

    @PostMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    @PreAuthorize("hasRole('ADMIN')") // Chỉ admin mới tạo được
    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody CreateNotificationRequest request) {
        Notification created = notificationService.createNotification(request);
        return ResponseEntity.ok(created);
    }
}
