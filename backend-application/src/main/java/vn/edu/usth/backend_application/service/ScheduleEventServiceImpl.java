package vn.edu.usth.backend_application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.ScheduleEventDto;
import vn.edu.usth.backend_application.entity.*;
import vn.edu.usth.backend_application.mapper.ScheduleEventMapper;
import vn.edu.usth.backend_application.repository.*;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleEventServiceImpl implements ScheduleEventService {

    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private UserRepository userRepository;
    private Doctor_AvailabilityRepository doctor_availabilityRepository;

    private ScheduleEventRepository scheduleEventRepository;

    @Override
    public ScheduleEventDto createScheduleEvent(ScheduleEventDto scheduleEventDto) {

        Schedule_Event schedule_event = ScheduleEventMapper.mapToSchedule(scheduleEventDto);

        Schedule_Event saveScheduleEvent = scheduleEventRepository.save(schedule_event);

        return ScheduleEventMapper.mapToScheduleDto(saveScheduleEvent);
    }

    @Override
    public List<ScheduleEventDto> getScheduleEventByPatientId(int patient_Id) {
        Patient patient = patientRepository.findByPatientId(patient_Id)
                .orElseThrow(
                        () -> new ResolutionException("Patient not found with Id: " + patient_Id));

        List<Schedule_Event> scheduleEventList = scheduleEventRepository.findByPatientId(patient);

        return scheduleEventList.stream().map(ScheduleEventMapper::mapToScheduleDto).collect(Collectors.toList());

    }

    @Override
    public List<ScheduleEventDto> getScheduleEventByDoctorId(int doctor_Id) {
        Doctor doctor = doctorRepository.findByDoctorId(doctor_Id)
                .orElseThrow(
                        () -> new ResolutionException("Doctor not found with Id: " + doctor_Id));

        List<Schedule_Event> scheduleEventList = scheduleEventRepository.findByDoctorId(doctor);

        return scheduleEventList.stream().map(ScheduleEventMapper::mapToScheduleDto).collect(Collectors.toList());
    }

    @Override
    public List<ScheduleEventDto> getAllScheduleEvents() {

        List<Schedule_Event> scheduleEventList = scheduleEventRepository.findAll();

        return scheduleEventList.stream().map(ScheduleEventMapper::mapToScheduleDto).collect(Collectors.toList());
    }

    @Override
    public ScheduleEventDto updateScheduleEventById(int ScheduleEventId, ScheduleEventDto scheduleEventDto) {
        Schedule_Event schedule_event = scheduleEventRepository.findById(ScheduleEventId)
                .orElseThrow(
                        () -> new ResolutionException("Schedule not found with Id: " + ScheduleEventId));

        schedule_event.setAppointmentStyle(scheduleEventDto.getAppointment_style());
        schedule_event.setAppointmentStatus(scheduleEventDto.getAppointment_status());

        Schedule_Event update_schedule = scheduleEventRepository.save(schedule_event);

        return ScheduleEventMapper.mapToScheduleDto(update_schedule);
    }


    @Override
    public void deleteScheduleEvent(int ScheduleEventId) {
        Schedule_Event schedule_event = scheduleEventRepository.findById(ScheduleEventId)
                .orElseThrow(
                        () -> new ResolutionException("Schedule event not found with id: " + ScheduleEventId));

        scheduleEventRepository.delete(schedule_event);
    }


}
