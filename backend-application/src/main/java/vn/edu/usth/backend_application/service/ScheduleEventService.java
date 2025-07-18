package vn.edu.usth.backend_application.service;

import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.ScheduleEventDto;

import java.util.List;

@Service
public interface ScheduleEventService {

    // Create Schedule Event
    ScheduleEventDto createScheduleEvent(ScheduleEventDto scheduleEventDto);

    // Get Schedule Event by PhoneNumber
    List<ScheduleEventDto> getScheduleEventByPatientId (int patient_id);

    List<ScheduleEventDto> getScheduleEventByDoctorId (int doctor_id);

    // Get all Schedule Event
    List<ScheduleEventDto> getAllScheduleEvents();

    // Update Schedule Event
    ScheduleEventDto updateScheduleEventById(int ScheduleEventId, ScheduleEventDto scheduleEventDto);

    // Delete Schedule Event
    void deleteScheduleEvent(int ScheduleEventId);

}
