package vn.edu.usth.backend_application.mapper;

import vn.edu.usth.backend_application.dto.NotificationDto;
import vn.edu.usth.backend_application.entity.Notification;
import vn.edu.usth.backend_application.entity.Schedule_Event;
import vn.edu.usth.backend_application.entity.User;

public class NotificationMapper {

    public static NotificationDto mapToNotificationDto(Notification notification) {
        return new NotificationDto(
                notification.getNotificationId(),
                notification.getSenderId().getId(),
                notification.getReceiverId().getId(),
                notification.getEventType(),
                notification.getScheduleEventId().getScheduleId(),
                notification.getMessage()
        );
    }

    public static Notification mapToNotification(NotificationDto notificationDto) {

        User sender = new User();
        sender.setId(notificationDto.getSender_id());

        User receiver = new User();
        receiver.setId(notificationDto.getReceiver_id());

        Schedule_Event event = new Schedule_Event();
        event.setScheduleId(notificationDto.getEvent_id());

        return new Notification(
                notificationDto.getNotification_id(),
                sender,
                receiver,
                notificationDto.getEventType(),
                event,
                notificationDto.getMessage()
        );
    }
}
