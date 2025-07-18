package vn.edu.usth.backend_application.service;

import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.PatientDto;

import java.util.List;

@Service
public interface PatientService {

    // Get Id of Patient By Id
    PatientDto getPatientById(int patientId);

    // Get all Patient
    List<PatientDto> getAllPatients(String name);

    // Delete Patient By Id
    void deletePatientById(int patientId);

}
