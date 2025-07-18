package vn.edu.usth.backend_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.Doctor_Availability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Doctor_AvailabilityRepository extends JpaRepository<Doctor_Availability, Integer> {

    List<Doctor_Availability> findByDoctor(Doctor doctor);

    Optional<Doctor_Availability> findByDoctorAndAvailabilityDate (Doctor doctor, LocalDate availabilityDate);

    List<Doctor_Availability> findByAvailabilityDate(LocalDate availabilityDate);

    Optional<Doctor_Availability> findTopByDoctorOrderByAvailabilityDateDesc(Doctor doctor);
}

