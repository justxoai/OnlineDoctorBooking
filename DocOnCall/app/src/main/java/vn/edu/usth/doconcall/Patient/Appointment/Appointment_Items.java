package vn.edu.usth.doconcall.Patient.Appointment;

public class Appointment_Items {

    private String date;
    private String time;
    private String doctorName;
    private int doctor_id;
    private int availability_id;
    private int slot_id;
    private int schedule_id;

    public Appointment_Items(String date, String time, String doctorName, int doctor_id, int availability_id, int slot_id, int schedule_id) {
        this.date = date;
        this.time = time;
        this.doctorName = doctorName;
        this.doctor_id = doctor_id;
        this.availability_id = availability_id;
        this.slot_id = slot_id;
        this.schedule_id = schedule_id;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getAvailability_id() {
        return availability_id;
    }

    public void setAvailability_id(int availability_id) {
        this.availability_id = availability_id;
    }

    public int getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }
}
