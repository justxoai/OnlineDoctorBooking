package vn.edu.usth.backend_application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.AvailabilitySlotDto;
import vn.edu.usth.backend_application.dto.Doctor_AvailabilityDto;
import vn.edu.usth.backend_application.entity.AvailabilitySlot;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.Doctor_Availability;
import vn.edu.usth.backend_application.entity.User;
import vn.edu.usth.backend_application.mapper.AvailabilitySlotMapper;
import vn.edu.usth.backend_application.mapper.DoctorAvailabilityMapper;
import vn.edu.usth.backend_application.repository.DoctorRepository;
import vn.edu.usth.backend_application.repository.Doctor_AvailabilityRepository;
import vn.edu.usth.backend_application.repository.UserRepository;

import javax.print.Doc;
import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {
    
    private DoctorRepository doctorRepository;

    private Doctor_AvailabilityRepository doctor_AvailabilityRepository;

    @Override
    public Doctor_AvailabilityDto createDoctorAvailabilityDto(Doctor_AvailabilityDto doctor_AvailabilityDto) {

        Doctor_Availability doctorAvailability = DoctorAvailabilityMapper.mapToDoctorAvailability(doctor_AvailabilityDto);

        Doctor_Availability savedDoctorAvailability = doctor_AvailabilityRepository.save(doctorAvailability);

        return DoctorAvailabilityMapper.mapToDoctorAvailabilityDto(savedDoctorAvailability);
    }

    @Override
    public Doctor_AvailabilityDto getDoctorAvailabilityDtoById(int id) {
        Doctor_Availability doctorAvailability = doctor_AvailabilityRepository.findById(id)
                .orElseThrow(
                        () -> new ResolutionException("Doctor Availability Date not found"));

        return DoctorAvailabilityMapper.mapToDoctorAvailabilityDto(doctorAvailability);
    }

    @Override
    public List<Doctor_AvailabilityDto> getAllDoctorAvailabilityDto() {
        List<Doctor_Availability> doctor_AvailabilityList = doctor_AvailabilityRepository.findAll();
        return doctor_AvailabilityList.stream().map((doctor_availability) -> DoctorAvailabilityMapper.mapToDoctorAvailabilityDto(doctor_availability)).collect(Collectors.toList());
    }

    @Override
    public List<Doctor_AvailabilityDto> getAllDoctorAvailabilityDtoByDoctorId(int doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(
                        () -> new ResolutionException("Doctor not found with ID: " + doctorId));

        List<Doctor_Availability> doctorAvailabilityList = doctor_AvailabilityRepository.findByDoctor(doctor);

        return doctorAvailabilityList.stream().map((doctor_availability) -> DoctorAvailabilityMapper.mapToDoctorAvailabilityDto(doctor_availability)).collect(Collectors.toList());
    }

    @Override
    public Doctor_AvailabilityDto updateDoctorAvailabilityDtoByDoctorIdAndDate(Doctor_AvailabilityDto doctor_AvailabilityDto, LocalDate date, int doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(
                        () -> new ResolutionException("Doctor not found with ID: " + doctorId));

        Doctor_Availability doctorAvailability = doctor_AvailabilityRepository.findByDoctorAndAvailabilityDate(doctor, date)
                .orElseThrow(
                        () -> new ResolutionException("Doctor not found with ID: " + doctorId));

        doctorAvailability.setAvailabilityDate(date);

        List<AvailabilitySlot> updateSlots = doctor_AvailabilityDto.getSlots().stream()
                .map(slotDto -> {
                    AvailabilitySlot slot = AvailabilitySlotMapper.mapToAvailabilitySlot(slotDto);
                    slot.setAvailabilityId(doctorAvailability);
                    return slot;
                }).collect(Collectors.toList());

        doctorAvailability.getSlots().clear();
        doctorAvailability.getSlots().addAll(updateSlots);

        Doctor_Availability savedDoctorAvailability = doctor_AvailabilityRepository.save(doctorAvailability);

        return DoctorAvailabilityMapper.mapToDoctorAvailabilityDto(savedDoctorAvailability);
    }

    @Override
    public void deleteDoctorAvailabilityDtoByDoctorId(int doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResolutionException("Doctor not found with ID: " + doctorId));

        List<Doctor_Availability> availabilities = doctor_AvailabilityRepository.findByDoctor(doctor);

        doctor_AvailabilityRepository.deleteAll(availabilities);
    }

    @Override
    public void deleteDoctorAvailabilityDtoByDoctorIdAndDate(int doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResolutionException("Doctor not found with ID: " + doctorId));

        Doctor_Availability availability = doctor_AvailabilityRepository.findByDoctorAndAvailabilityDate(doctor, date)
                .orElseThrow(() -> new ResolutionException("Availability not found for doctor on " + date));

        doctor_AvailabilityRepository.delete(availability);
    }

    @Override
    public void deleteDoctorAvailabilityDtoById(int id) {
        Doctor_Availability doctorAvailability = doctor_AvailabilityRepository.findById(id)
                .orElseThrow(
                        () -> new ResolutionException("Doctor Availability Date not found"));

        doctor_AvailabilityRepository.delete(doctorAvailability);
    }
}
