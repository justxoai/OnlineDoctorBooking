package vn.edu.usth.backend_application.service;

import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.NotificationDto;

import java.util.List;

@Service
public interface NotificationService {


    // Create Notification
    NotificationDto createNotification(NotificationDto notificationDto);

    // Get Id of Notification
    NotificationDto getNotification(int NotificationId);

    // Get all Notification
    List<NotificationDto> getAllNotifications();

    // Update Notification
    NotificationDto updateNotification(int NotificationId, NotificationDto notificationDto);

    // Delete Notification
    void deleteNotification(int NotificationId);
}
