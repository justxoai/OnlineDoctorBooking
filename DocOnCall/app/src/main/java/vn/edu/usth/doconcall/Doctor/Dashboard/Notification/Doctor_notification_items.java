package vn.edu.usth.doconcall.Doctor.Dashboard.Notification;

public class Doctor_notification_items {

    String header;
    String content;

    public Doctor_notification_items(String header, String content){
        this.header = header;
        this.content = content;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }
}
