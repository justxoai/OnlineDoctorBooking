package vn.edu.usth.backend_application.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.usth.backend_application.enums.Gender;
import vn.edu.usth.backend_application.enums.Role;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    // User Entity - Patient Entity
    private String username;
    private String password;
    private String phone;
    private LocalDate dob;
    private Gender gender;
    private Role role;

    // Doctor Entity
    private String specialization;
    private String year_experience;
    private String work_process;
}
