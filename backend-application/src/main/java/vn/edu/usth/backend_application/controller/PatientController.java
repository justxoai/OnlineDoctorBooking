package vn.edu.usth.backend_application.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.backend_application.dto.PatientDto;
import vn.edu.usth.backend_application.service.PatientService;

import java.util.List;

@RestController
@PreAuthorize("hasRole('PATIENT')")
@RequestMapping("/backend/patient/")
@AllArgsConstructor
public class PatientController {

    private PatientService patientService;

    // Build GET Patient REST API
    @GetMapping("/get/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable("id") int patientId) {
        PatientDto patientDto = patientService.getPatientById(patientId);
        return ResponseEntity.ok(patientDto);
    }

    // Build GET All Patient REST API
    @GetMapping("/get-all-patient")
    public ResponseEntity<List<PatientDto>> getAllPatients(@RequestParam(required = false) String name) {
        List<PatientDto> patients = patientService.getAllPatients(name);
        return ResponseEntity.ok(patients);
    }

    // Build DELETE Patient REST API
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable("id") int patientId) {
        patientService.deletePatientById(patientId);
        return ResponseEntity.ok("Patient deleted successfully");
    }
}
