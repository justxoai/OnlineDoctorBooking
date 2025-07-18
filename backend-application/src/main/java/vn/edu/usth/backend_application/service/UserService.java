package vn.edu.usth.backend_application.service;

import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.UserDto;

import java.util.List;

@Service
public interface UserService {

    // Get Id of User By Id
    UserDto getUserById(int id);

    // Get all User
    List<UserDto> getAllUsers(String name);

    // Update User By Id
    UserDto updateUserById(int id, UserDto user);

    // Delete User By Id
    void deleteUserById(int id);

}
