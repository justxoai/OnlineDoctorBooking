package vn.edu.usth.backend_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.usth.backend_application.entity.Patient;
import vn.edu.usth.backend_application.entity.User;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByUserId (User user);

    Optional<Patient> findByPatientId (int patientId);

    List<Patient> findByUserId_NameContainingIgnoreCase (String name);
}
