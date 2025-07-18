package vn.edu.usth.doconcall.Patient.Dashboard.Notification;

public class Patient_notification_item {

    String header;
    String content;

    public Patient_notification_item(String header, String content){
        this.header = header;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getHeader() {
        return header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
