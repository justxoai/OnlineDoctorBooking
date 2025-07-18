package vn.edu.usth.backend_application.mapper;

import vn.edu.usth.backend_application.dto.ScheduleEventDto;
import vn.edu.usth.backend_application.entity.*;

public class ScheduleEventMapper {

    public static ScheduleEventDto mapToScheduleDto(Schedule_Event schedule_event) {
        return new ScheduleEventDto(
                schedule_event.getScheduleId(),
                schedule_event.getPatientId().getPatientId(),
                schedule_event.getDoctorId().getDoctorId(),
                schedule_event.getDoctorAvailabilityId().getDoctorAvailabilityId(),
                schedule_event.getSlotId().getSlotId(),
                schedule_event.getAppointmentStyle(),
                schedule_event.getAppointmentStatus()
        );
    }

    public static Schedule_Event mapToSchedule(ScheduleEventDto scheduleEventDto) {
        Patient patient = new Patient();
        patient.setPatientId(scheduleEventDto.getPatient_id());

        Doctor doctor = new Doctor();
        doctor.setDoctorId(scheduleEventDto.getDoctor_id());

        Doctor_Availability doctorAvailability = new Doctor_Availability();
        doctorAvailability.setDoctorAvailabilityId(scheduleEventDto.getDoctor_availability_id());

        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setSlotId(scheduleEventDto.getTime_slot_id());

        return new Schedule_Event(
                scheduleEventDto.getSchedule_id(),
                patient,
                doctor,
                doctorAvailability,
                slot,
                scheduleEventDto.getAppointment_style(),
                scheduleEventDto.getAppointment_status()
        );
    }

}
