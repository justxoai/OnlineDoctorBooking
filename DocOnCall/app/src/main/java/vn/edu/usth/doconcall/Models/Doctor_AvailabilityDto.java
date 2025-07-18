package vn.edu.usth.doconcall.Models;

import java.util.List;

public class Doctor_AvailabilityDto {

    private int availability_id;

    private int doctor_id;

    private String availability_date;

    private List<AvailabilitySlotDto> slots;

    public Doctor_AvailabilityDto() {
    }

    public Doctor_AvailabilityDto(int availability_id, int doctor_id, String availability_date, List<AvailabilitySlotDto> slots) {
        this.availability_id = availability_id;
        this.doctor_id = doctor_id;
        this.availability_date = availability_date;
        this.slots = slots;
    }

    public int getAvailability_id() {
        return availability_id;
    }

    public void setAvailability_id(int availability_id) {
        this.availability_id = availability_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getAvailability_date() {
        return availability_date;
    }

    public void setAvailability_date(String availability_date) {
        this.availability_date = availability_date;
    }

    public List<AvailabilitySlotDto> getSlots() {
        return slots;
    }

    public void setSlots(List<AvailabilitySlotDto> slots) {
        this.slots = slots;
    }
}
