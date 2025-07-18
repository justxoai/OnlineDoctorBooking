package vn.edu.usth.backend_application.service;

import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.DoctorDto;

import java.util.List;

@Service
public interface DoctorService {

    // Get Id of Doctor By Id
    DoctorDto getDoctorById(int Id);

    // Get all Doctor
    List<DoctorDto> getAllDoctors(String name);

    // Update Doctor By Id
    DoctorDto updateDoctorById(int Id, DoctorDto doctorDto);

    // Delete Doctor By Id
    void deleteDoctorById(int Id);
}
