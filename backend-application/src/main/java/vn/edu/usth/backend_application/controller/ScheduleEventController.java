package vn.edu.usth.backend_application.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.backend_application.dto.ScheduleEventDto;
import vn.edu.usth.backend_application.entity.Schedule_Event;
import vn.edu.usth.backend_application.mapper.ScheduleEventMapper;
import vn.edu.usth.backend_application.service.ScheduleEventService;

import java.util.List;

@RestController
@RequestMapping("/backend/schedule-event")
@AllArgsConstructor
public class ScheduleEventController {

    private ScheduleEventService scheduleEventService;

    // Build CREATE Schedule-event REST API
    @PostMapping
    public ResponseEntity<ScheduleEventDto> createScheduleEvent(@RequestBody ScheduleEventDto scheduleEventDto) {
        ScheduleEventDto schedule_event = scheduleEventService.createScheduleEvent(scheduleEventDto);
        return new ResponseEntity<>(schedule_event, HttpStatus.CREATED);
    }

    // Build GET Schedule-event REST API
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<ScheduleEventDto>> getAllScheduleEventByPatientId(@PathVariable("id") int patientId) {

        List<ScheduleEventDto> schedule_event = scheduleEventService.getScheduleEventByPatientId(patientId);

        return ResponseEntity.ok(schedule_event);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<ScheduleEventDto>> getAllScheduleEventByDoctorId(@PathVariable("id") int doctorId) {

        List<ScheduleEventDto> schedule_event = scheduleEventService.getScheduleEventByDoctorId(doctorId);

        return ResponseEntity.ok(schedule_event);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ScheduleEventDto> updateScheduleEventById(@PathVariable("id") int ScheduleEventId, @RequestBody ScheduleEventDto scheduleEventDto) {
        ScheduleEventDto updateScheduleEvent = scheduleEventService.updateScheduleEventById(ScheduleEventId, scheduleEventDto);
        return ResponseEntity.ok(updateScheduleEvent);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ScheduleEventDto>> getAllScheduleEvent(){

        List<ScheduleEventDto> scheduleEventDtos = scheduleEventService.getAllScheduleEvents();

        return ResponseEntity.ok(scheduleEventDtos);
    }

    // Build DELETE Schedule-Event REST API
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteScheduleEventById(@PathVariable("id") int id){

        scheduleEventService.deleteScheduleEvent(id);
        return new ResponseEntity<>("Schedule event deleted successfully", HttpStatus.OK);
    }

}
