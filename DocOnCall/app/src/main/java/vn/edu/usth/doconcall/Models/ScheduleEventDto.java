package vn.edu.usth.doconcall.Models;

public class ScheduleEventDto {

    private int schedule_id;

    private int patient_id;

    private int doctor_id;

    private int doctor_availability_id;

    private int time_slot_id;
    private String appointment_style;

    private String appointment_status;

    public ScheduleEventDto() {
    }

    public ScheduleEventDto(int schedule_id, int patient_id, int doctor_id, int doctor_availability_id, int time_slot_id, String appointment_style, String appointment_status) {
        this.schedule_id = schedule_id;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.doctor_availability_id = doctor_availability_id;
        this.time_slot_id = time_slot_id;
        this.appointment_style = appointment_style;
        this.appointment_status = appointment_status;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getDoctor_availability_id() {
        return doctor_availability_id;
    }

    public void setDoctor_availability_id(int doctor_availability_id) {
        this.doctor_availability_id = doctor_availability_id;
    }

    public String getAppointment_style() {
        return appointment_style;
    }

    public void setAppointment_style(String appointment_style) {
        this.appointment_style = appointment_style;
    }

    public String getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
    }

    public int getTime_slot_id() {
        return time_slot_id;
    }

    public void setTime_slot_id(int time_slot_id) {
        this.time_slot_id = time_slot_id;
    }
}
