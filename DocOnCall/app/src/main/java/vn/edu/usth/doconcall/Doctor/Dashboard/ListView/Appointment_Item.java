package vn.edu.usth.doconcall.Doctor.Dashboard.ListView;

public class Appointment_Item {
    private String appointment_time;
    private String patient_name;
    private String appointment_type;
    private String appointment_status;

    public Appointment_Item(String time, String patient_name, String appointment_type, String appointment_status) {
        this.appointment_time = time;
        this.patient_name = patient_name;
        this.appointment_type = appointment_type;
        this.appointment_status = appointment_status;
    }

    public void setTime(String time) {
        this.appointment_time = time;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public void setAppointment_type(String appointment_type) {
        this.appointment_type = appointment_type;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
    }

    public String getPatient_name() {
        return patient_name;
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