package vn.edu.usth.backend_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.usth.backend_application.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findById(int id);

    List<User> findByNameContainingIgnoreCase(String name);

    boolean existsByPhoneNumber(String phoneNumber);
}
