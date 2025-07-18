package vn.edu.usth.backend_application.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.backend_application.dto.NotificationDto;
import vn.edu.usth.backend_application.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/backend/notification")
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationDto notificationDto) {
        NotificationDto savedNotificationDto = notificationService.createNotification(notificationDto);
        return new ResponseEntity<>(savedNotificationDto, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<NotificationDto> getNotification(@PathVariable("id") int notificationId) {
        NotificationDto notificationDto = notificationService.getNotification(notificationId);
        return ResponseEntity.ok(notificationDto);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> notificationDtos = notificationService.getAllNotifications();
        return ResponseEntity.ok(notificationDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NotificationDto> updateNotification(@PathVariable("id") int notificationId,
                                                              @RequestBody NotificationDto notificationDto) {
        NotificationDto updatedNotificationDto = notificationService.updateNotification(notificationId, notificationDto);
        return ResponseEntity.ok(updatedNotificationDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable("id") int notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification delete");
    }
}
