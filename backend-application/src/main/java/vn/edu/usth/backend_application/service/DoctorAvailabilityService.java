package vn.edu.usth.backend_application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.usth.backend_application.dto.Doctor_AvailabilityDto;

import java.time.LocalDate;
import java.util.List;

@Service
public interface DoctorAvailabilityService {

    // Create Doctor-Avail
    Doctor_AvailabilityDto createDoctorAvailabilityDto (Doctor_AvailabilityDto doctor_AvailabilityDto);

    // Get Avail by DoctorId and Date

    // Get Id of Availability
    Doctor_AvailabilityDto getDoctorAvailabilityDtoById(int id);

    // Get all Availability
    List<Doctor_AvailabilityDto> getAllDoctorAvailabilityDto();

    // Get all Availability Base on DoctorId
    List<Doctor_AvailabilityDto> getAllDoctorAvailabilityDtoByDoctorId(int doctorId);

    // Update Avail by DoctorId
    Doctor_AvailabilityDto updateDoctorAvailabilityDtoByDoctorIdAndDate(Doctor_AvailabilityDto doctor_AvailabilityDto, LocalDate date, int doctorId);

    // Delete Availability
    void deleteDoctorAvailabilityDtoByDoctorId(int doctorId);

    void deleteDoctorAvailabilityDtoByDoctorIdAndDate(int doctorId, LocalDate date);

    void deleteDoctorAvailabilityDtoById(int id);
}
