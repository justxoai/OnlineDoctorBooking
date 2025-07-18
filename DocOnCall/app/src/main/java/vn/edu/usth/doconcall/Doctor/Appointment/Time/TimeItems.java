package vn.edu.usth.doconcall.Doctor.Appointment.Time;

public class TimeItems {

    String time;

    int doctor_id;

    int availability_id;

    int slot_id;

    public TimeItems(String time, int doctor_id, int availability_id, int slot_id) {
        this.time = time;
        this.doctor_id = doctor_id;
        this.availability_id = availability_id;
        this.slot_id = slot_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setAvailability_id(int availability_id) {
        this.availability_id = availability_id;
    }

    public int getAvailability_id() {
        return availability_id;
    }

    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }

    public int getSlot_id() {
        return slot_id;
    }
}
