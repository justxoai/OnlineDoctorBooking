package vn.edu.usth.backend_application.mapper;

import vn.edu.usth.backend_application.dto.AvailabilitySlotDto;
import vn.edu.usth.backend_application.dto.Doctor_AvailabilityDto;
import vn.edu.usth.backend_application.entity.AvailabilitySlot;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.Doctor_Availability;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorAvailabilityMapper {

    public static Doctor_AvailabilityDto mapToDoctorAvailabilityDto(Doctor_Availability doctor_availability) {

        return new Doctor_AvailabilityDto(
                doctor_availability.getDoctorAvailabilityId(),
                doctor_availability.getDoctor().getDoctorId(),
                doctor_availability.getAvailabilityDate(),
                doctor_availability.getSlots().stream().map(AvailabilitySlotMapper::mapToAvailabilitySlotDto).collect(Collectors.toList())
        );
    }

    public static Doctor_Availability mapToDoctorAvailability(Doctor_AvailabilityDto doctorAvailabilityDto) {

        Doctor_Availability doctorAvailability = new Doctor_Availability(
                doctorAvailabilityDto.getAvailability_id(),
                Doctor.builder().doctorId(doctorAvailabilityDto.getDoctor_id()).build(),
                doctorAvailabilityDto.getAvailability_date(),
                new ArrayList<>()
        );

        List<AvailabilitySlot> slots = doctorAvailabilityDto.getSlots().stream()
                .map(slotDto -> {
                    AvailabilitySlot slot = AvailabilitySlotMapper.mapToAvailabilitySlot(slotDto);
                    slot.setAvailabilityId(doctorAvailability); // this sets the foreign key
                    return slot;
                })
                .collect(Collectors.toList());

        doctorAvailability.setSlots(slots);

        return doctorAvailability;
    }
}
