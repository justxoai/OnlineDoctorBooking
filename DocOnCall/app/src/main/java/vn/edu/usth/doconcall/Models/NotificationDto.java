package vn.edu.usth.doconcall.Models;

public class NotificationDto {

    private int notification_id;

    private int sender_id;

    private int receiver_id;

    private String eventType;

    private int event_id;

    private String message;

    public NotificationDto(){}

    public NotificationDto(int notification_id, int sender_id, int receiver_id, String eventType, int event_id, String message){
        this.notification_id = notification_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.eventType = eventType;
        this.event_id = event_id;
        this.message = message;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
