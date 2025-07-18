package vn.edu.usth.backend_application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.NotificationDto;
import vn.edu.usth.backend_application.entity.Notification;
import vn.edu.usth.backend_application.entity.Schedule_Event;
import vn.edu.usth.backend_application.entity.User;
import vn.edu.usth.backend_application.exception.ResourceNotFoundException;
import vn.edu.usth.backend_application.mapper.NotificationMapper;
import vn.edu.usth.backend_application.repository.NotificationRepository;
import vn.edu.usth.backend_application.repository.ScheduleEventRepository;
import vn.edu.usth.backend_application.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private UserRepository userRepository;

    private NotificationRepository notificationRepository;

    private ScheduleEventRepository scheduleEventRepository;

    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {

        Notification notification = NotificationMapper.mapToNotification(notificationDto);

        Notification savedNotification = notificationRepository.save(notification);

        return NotificationMapper.mapToNotificationDto(savedNotification);
    }

    @Override
    public NotificationDto getNotification(int NotificationId) {
        Notification notification = notificationRepository.findById(NotificationId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Notification not found with ID: " + NotificationId));

        return NotificationMapper.mapToNotificationDto(notification);
    }

    @Override
    public List<NotificationDto> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream().map(NotificationMapper::mapToNotificationDto).collect(Collectors.toList());
    }

    @Override
    public NotificationDto updateNotification(int NotificationId, NotificationDto notificationDto) {
        Notification notification = notificationRepository.findById(NotificationId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Notification not found with ID: " + NotificationId));

        notification.setEventType(notificationDto.getEventType());
        notification.setMessage(notificationDto.getMessage());

        Notification savedNotification = notificationRepository.save(notification);

        return NotificationMapper.mapToNotificationDto(savedNotification);
    }

    @Override
    public void deleteNotification(int NotificationId) {
        Notification notification = notificationRepository.findById(NotificationId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Notification not found with ID: " + NotificationId));

        notificationRepository.delete(notification);
    }
}
