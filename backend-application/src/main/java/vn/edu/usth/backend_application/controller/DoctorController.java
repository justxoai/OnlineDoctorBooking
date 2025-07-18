package vn.edu.usth.backend_application.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.backend_application.dto.DoctorDto;
import vn.edu.usth.backend_application.service.DoctorService;

import java.util.List;

@RestController
@PreAuthorize("hasRole('DOCTOR')")
@RequestMapping("/backend/doctor/")
@AllArgsConstructor
public class DoctorController {

    private DoctorService doctorService;

    // Build GET Doctor REST API
    @GetMapping("/get/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable("id") int doctorId) {
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctorDto);
    }

    // Build GET All Doctor REST API
    @GetMapping("/get-all-doctor")
    public ResponseEntity<List<DoctorDto>> getAllDoctors(@RequestParam(required = false) String name) {
        List<DoctorDto> doctors = doctorService.getAllDoctors(name);
        return ResponseEntity.ok(doctors);
    }

    // Build PUT Update User REST API
    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorDto> updateDoctorById(@PathVariable("id") int doctorId,
                                                      @RequestBody DoctorDto doctorDto) {
        DoctorDto updateDoctor = doctorService.updateDoctorById(doctorId, doctorDto);
        return ResponseEntity.ok(updateDoctor);
    }

    // Build DELETE User REST API
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable("id") int doctorId) {
        doctorService.deleteDoctorById(doctorId);
        return ResponseEntity.ok("Doctor deleted successfully");
    }
}
