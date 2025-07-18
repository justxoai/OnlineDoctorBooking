package vn.edu.usth.backend_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.usth.backend_application.enums.EventTypeNotification;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "new_notification_table")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    @ManyToOne
    @JoinColumn(name = "Sender_id", nullable = false)
    private User senderId;

    @ManyToOne
    @JoinColumn(name = "Receiver_id", nullable = false)
    private User receiverId;

    @Column(name = "Event_type")
    private EventTypeNotification eventType;

    @ManyToOne
    @JoinColumn(name = "Event_id", nullable = false)
    private Schedule_Event scheduleEventId;

    @Column(name = "Message")
    private String message;
}
