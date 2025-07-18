package vn.edu.usth.backend_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.User;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Optional<Doctor> findByUserId (User user);

    Optional<Doctor> findByDoctorId (int userId);

    List<Doctor> findByUserId_NameContainingIgnoreCase (String name);
}
