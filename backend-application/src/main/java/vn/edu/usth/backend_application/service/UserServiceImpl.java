package vn.edu.usth.backend_application.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.UserDto;
import vn.edu.usth.backend_application.entity.User;
import vn.edu.usth.backend_application.mapper.UserMapper;
import vn.edu.usth.backend_application.repository.UserRepository;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserDto getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResolutionException("User not found with ID: " + userId));

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers(String name) {
        List<User> users;

        if (name != null && !name.isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCase(name);
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUserById(int userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResolutionException("User not found with ID: " + userId));

        user.setName(userDto.getName());
        user.setBirthday(userDto.getBirthday());
        user.setGender(userDto.getGender());

        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public void deleteUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResolutionException("User don't exist with ID: " + userId)
                );

        userRepository.delete(user);
    }

}
