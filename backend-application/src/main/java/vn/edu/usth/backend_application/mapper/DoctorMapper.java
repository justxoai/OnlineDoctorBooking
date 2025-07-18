package vn.edu.usth.backend_application.mapper;

import vn.edu.usth.backend_application.dto.DoctorDto;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.User;

public class DoctorMapper {

    public static DoctorDto mapToDoctorDto(Doctor doctor){
        User user = doctor.getUserId();
        return new DoctorDto(
                doctor.getDoctorId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBirthday(),
                user.getGender(),
                doctor.getSpecialization(),
                doctor.getYearExperience(),
                doctor.getWorkProcess()
        );
    }
}
