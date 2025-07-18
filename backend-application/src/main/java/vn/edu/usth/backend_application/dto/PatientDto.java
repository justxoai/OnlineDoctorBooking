package vn.edu.usth.backend_application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.usth.backend_application.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

    private int id;
    private String name;
    private String phoneNumber;
    private LocalDate birthday;
    private Gender gender;
}
