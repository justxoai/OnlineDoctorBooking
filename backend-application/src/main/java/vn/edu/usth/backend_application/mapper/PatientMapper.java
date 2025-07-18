package vn.edu.usth.backend_application.mapper;

import vn.edu.usth.backend_application.dto.PatientDto;
import vn.edu.usth.backend_application.entity.Patient;
import vn.edu.usth.backend_application.entity.User;

public class PatientMapper {

    public static PatientDto mapToPatientDto(Patient patient) {
        User user = patient.getUserId();
        return new PatientDto(
                patient.getPatientId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBirthday(),
                user.getGender()
        );
    }

}
