package vn.edu.usth.backend_application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.PatientDto;
import vn.edu.usth.backend_application.entity.Patient;
import vn.edu.usth.backend_application.entity.User;
import vn.edu.usth.backend_application.mapper.PatientMapper;
import vn.edu.usth.backend_application.repository.PatientRepository;
import vn.edu.usth.backend_application.repository.UserRepository;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private UserRepository userRepository;

    private PatientRepository patientRepository;

    @Override
    public PatientDto getPatientById(int patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResolutionException("Patient not found with ID: " + patientId));

        return PatientMapper.mapToPatientDto(patient);
    }

    @Override
    public List<PatientDto> getAllPatients(String name) {
        List<Patient> patients;

        if (name != null && !name.isEmpty()) {
            patients = patientRepository.findByUserId_NameContainingIgnoreCase(name); // ← Needs to be defined in repository
        } else {
            patients = patientRepository.findAll();
        }

        return patients.stream()
                .map(PatientMapper::mapToPatientDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePatientById(int patientId) {
        User user = userRepository.findById(patientId)
                .orElseThrow(() -> new ResolutionException("Patient not found with ID: " + patientId));

        userRepository.delete(user);
    }
}
