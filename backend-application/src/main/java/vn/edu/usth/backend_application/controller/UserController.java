package vn.edu.usth.backend_application.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.backend_application.dto.UserDto;
import vn.edu.usth.backend_application.repository.UserRepository;
import vn.edu.usth.backend_application.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/backend/user/")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private UserService userService;

    // Build GET User REST API
    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") int userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Build GET all User REST API
    @GetMapping("/get-all-user")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false) String name) {
        List<UserDto> users = userService.getAllUsers(name);
        return ResponseEntity.ok(users);
    }

    // Build PUT Update User REST API
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable("id") int userId,
                                                  @RequestBody UserDto user) {
        UserDto updatedUser = userService.updateUserById(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/check/{phoneNumber}")
    public ResponseEntity<Boolean> phoneExists(@PathVariable("phoneNumber") String phoneNumber) {
        boolean exists = userRepository.existsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(exists);
    }

}
