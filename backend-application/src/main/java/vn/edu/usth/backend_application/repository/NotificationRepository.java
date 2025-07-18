package vn.edu.usth.backend_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.usth.backend_application.entity.Notification;
import vn.edu.usth.backend_application.entity.User;
import vn.edu.usth.backend_application.enums.EventTypeNotification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Optional<Notification> findById(int id);

    Optional<Notification> findBySenderId(User senderId);

    Optional<Notification> findByReceiverId(User receiverId);

    List<Notification> findByEventType(EventTypeNotification eventType);

}
