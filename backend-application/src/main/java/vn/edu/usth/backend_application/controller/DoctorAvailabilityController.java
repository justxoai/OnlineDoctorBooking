package vn.edu.usth.backend_application.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.backend_application.dto.Doctor_AvailabilityDto;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.Doctor_Availability;
import vn.edu.usth.backend_application.service.DoctorAvailabilityService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/backend/doctor-avail")
@AllArgsConstructor
public class DoctorAvailabilityController {

    private DoctorAvailabilityService doctorAvailabilityService;

    // Build CREATE Doctor-Availability REST API
    @PostMapping
    public ResponseEntity<Doctor_AvailabilityDto> createDoctorAvailability(@RequestBody Doctor_AvailabilityDto doctor_availabilityDto) {
        Doctor_AvailabilityDto doctorAvailability = doctorAvailabilityService.createDoctorAvailabilityDto(doctor_availabilityDto);
        return new ResponseEntity<>(doctorAvailability, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Doctor_AvailabilityDto> getDoctorAvailabilityById(@PathVariable("id") int id) {
        Doctor_AvailabilityDto doctorAvailabilityDto = doctorAvailabilityService.getDoctorAvailabilityDtoById(id);
        return ResponseEntity.ok(doctorAvailabilityDto);

    }

    // Build GET all Doctor-Availability REST API
    @GetMapping("/get-all-avail")
    public ResponseEntity<List<Doctor_AvailabilityDto>> getAllDoctorAvailability() {

        List<Doctor_AvailabilityDto> doctorAvailabilityDtos = doctorAvailabilityService.getAllDoctorAvailabilityDto();

        return ResponseEntity.ok(doctorAvailabilityDtos);
    }

    @GetMapping("/get-all-avail/{id}")
    public ResponseEntity<List<Doctor_AvailabilityDto>> getAllDoctorAvailabilityByDoctorId(@PathVariable("id") int doctorId) {

        List<Doctor_AvailabilityDto> doctorAvailabilityDtos = doctorAvailabilityService.getAllDoctorAvailabilityDtoByDoctorId(doctorId);

        return ResponseEntity.ok(doctorAvailabilityDtos);
    }

    // Build UPDATE Doctor-Availability REST API
    @PutMapping("/update-id-date/{id}/{date}")
    public ResponseEntity<Doctor_AvailabilityDto> updateDoctorAvailabilityByDoctorIdAndDate(@PathVariable("id") int doctorId,
                                                                               @PathVariable("date") LocalDate date, @RequestBody Doctor_AvailabilityDto doctor_availabilityDto) {
        Doctor_AvailabilityDto doctorAvailabilityDto = doctorAvailabilityService.updateDoctorAvailabilityDtoByDoctorIdAndDate(doctor_availabilityDto, date, doctorId);

        return ResponseEntity.ok(doctorAvailabilityDto);
    }

    // Build DELETE Doctor-Availability REST API
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctorAvailabilityByDoctorId(@PathVariable("id") int doctorId) {
        doctorAvailabilityService.deleteDoctorAvailabilityDtoByDoctorId(doctorId);
        return ResponseEntity.ok("Doctor Avail deleted");
    }

    @DeleteMapping("delete-id-date/{id}/{date}")
    public ResponseEntity<String> deleteDoctorAvailabilityByDoctorIdAndDate(@PathVariable("id") int doctorId,
                                                                          @PathVariable("date") LocalDate date) {
        doctorAvailabilityService.deleteDoctorAvailabilityDtoByDoctorIdAndDate(doctorId, date);
        return ResponseEntity.ok("Doctor Avail deleted");
    }
}
