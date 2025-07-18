package vn.edu.usth.backend_application.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.enums.Role;
import vn.edu.usth.backend_application.config.JwtService;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.Patient;
import vn.edu.usth.backend_application.entity.User;
import vn.edu.usth.backend_application.repository.DoctorRepository;
import vn.edu.usth.backend_application.repository.PatientRepository;
import vn.edu.usth.backend_application.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhone())
                .birthday(request.getDob())
                .gender(request.getGender())
                .role(request.getRole())
                .build();


        if (request.getRole() == Role.DOCTOR) {
            var doctor = Doctor.builder()
                    .userId(user)
                    .specialization(request.getSpecialization())
                    .yearExperience(request.getYear_experience())
                    .workProcess(request.getWork_process())
                    .build();
            doctorRepository.save(doctor);
        }else if (request.getRole() == Role.PATIENT) {
            var patient = Patient.builder()
                    .userId(user)
                    .build();
            patientRepository.save(patient);
        }

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + request.getPhoneNumber()));

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).role(user.getRole().name()).userId(user.getId()).userName(user.getName()).build();
    }
}

