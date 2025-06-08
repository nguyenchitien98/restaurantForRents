package com.tien.restaurant.service;

import com.tien.restaurant.dto.request.CreateNotificationRequest;
import com.tien.restaurant.dto.response.NotificationResponse;
import com.tien.restaurant.entity.Notification;
import com.tien.restaurant.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    public Notification createNotification(CreateNotificationRequest request) {
        Notification notification = new Notification();
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return repository.save(notification);
    }

    public List<NotificationResponse> getAll() {
        return repository.findAll().stream()
                .map(n -> new NotificationResponse(
                        n.getId(),
                        n.getMessage(),
                        n.getType(),
                        n.isRead() // đúng kiểu boolean
                ))
                .collect(Collectors.toList());
    }

    public Notification markAsRead(Long id) {
        var notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        notification.setRead(true);
        return repository.save(notification);
    }
}
