package vn.edu.usth.doconcall.Doctor.Appointment.Date;

public class DateItems {

    String date;

    int doctor_id;

    int avail_id;

    public DateItems(String date, int doctor_id, int avail_id) {
        this.date = date;
        this.doctor_id = doctor_id;
        this.avail_id = avail_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public int getAvail_id() {
        return avail_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setAvail_id(int avail_id) {
        this.avail_id = avail_id;
    }
}
