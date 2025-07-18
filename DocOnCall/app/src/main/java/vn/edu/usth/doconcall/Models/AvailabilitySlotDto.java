package vn.edu.usth.doconcall.Models;

public class AvailabilitySlotDto {

    private int slot_id;

    private String slot_time;

    private String slot_status;

    public AvailabilitySlotDto(){}

    public AvailabilitySlotDto(int slot_id, String slot_time, String slot_status) {
        this.slot_id = slot_id;
        this.slot_time = slot_time;
        this.slot_status = slot_status;
    }

    public int getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }

    public String getSlot_time() {
        return slot_time;
    }

    public void setSlot_time(String slot_time) {
        this.slot_time = slot_time;
    }

    public void setSlot_status(String slot_status) {
        this.slot_status = slot_status;
    }

    public String getSlot_status() {
        return slot_status;
    }
}
