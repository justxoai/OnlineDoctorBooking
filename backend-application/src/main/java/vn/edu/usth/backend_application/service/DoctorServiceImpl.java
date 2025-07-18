package vn.edu.usth.backend_application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.DoctorDto;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.User;
import vn.edu.usth.backend_application.mapper.DoctorMapper;
import vn.edu.usth.backend_application.repository.DoctorRepository;
import vn.edu.usth.backend_application.repository.UserRepository;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService{

    private UserRepository userRepository;

    private DoctorRepository doctorRepository;

    @Override
    public DoctorDto getDoctorById(int doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResolutionException("Doctor not found with ID: " + doctorId));

        return DoctorMapper.mapToDoctorDto(doctor);
    }

    @Override
    public List<DoctorDto> getAllDoctors(String name) {
        List<Doctor> doctors;

        if (name != null && !name.isEmpty()) {
            doctors = doctorRepository.findByUserId_NameContainingIgnoreCase(name);
        } else {
            doctors = doctorRepository.findAll();
        }

        return doctors.stream()
                .map(DoctorMapper::mapToDoctorDto)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDto updateDoctorById(int doctorId, DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResolutionException("Doctor not found with ID: " + doctorId));

        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setYearExperience(doctorDto.getYear_experience());
        doctor.setWorkProcess(doctorDto.getWork_experience());

        Doctor saveDoctor = doctorRepository.save(doctor);

        return DoctorMapper.mapToDoctorDto(saveDoctor);
    }

    @Override
    public void deleteDoctorById(int doctorId) {
        User user = userRepository.findById(doctorId)
                .orElseThrow(() -> new ResolutionException("Doctor not found with ID: " + doctorId));

        userRepository.delete(user);
    }
}
