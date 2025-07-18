package vn.edu.usth.backend_application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.usth.backend_application.enums.EventTypeNotification;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private int notification_id;
    private int sender_id;
    private int receiver_id;
    private EventTypeNotification eventType;
    private int event_id;
    private String message;
}
