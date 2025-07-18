package vn.edu.usth.doconcall.Patient.Dashboard.ListView;

public class AppointmentItem {
    private String appointment_time;
    private String doctor_name;
    private String appointment_type;
    private String appointment_status;

    public AppointmentItem(String time, String doctor_name, String appointment_type, String appointment_status) {
        this.appointment_time = time;
        this.doctor_name = doctor_name;
        this.appointment_type = appointment_type;
        this.appointment_status = appointment_status;
    }

    public void setTime(String time) {
        this.appointment_time = time;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public void setAppointment_type(String appointment_type) {
        this.appointment_type = appointment_type;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getTime() {
        return appointment_time;
    }

    public String getAppointment_type() {
        return appointment_type;
    }

    public String getAppointment_status() {
        return appointment_status;
    }
}
