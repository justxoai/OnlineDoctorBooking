package vn.edu.usth.backend_application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.usth.backend_application.enums.Appointment_Status;
import vn.edu.usth.backend_application.enums.Appointment_Style;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEventDto {

    private int schedule_id;

    private int patient_id;

    private int doctor_id;

    private int doctor_availability_id;

    private int time_slot_id;

    private Appointment_Style appointment_style;
    private Appointment_Status appointment_status;
}
