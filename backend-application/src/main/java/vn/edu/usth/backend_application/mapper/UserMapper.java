package vn.edu.usth.backend_application.mapper;

import vn.edu.usth.backend_application.dto.UserDto;
import vn.edu.usth.backend_application.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBirthday(),
                user.getGender()
        );
    }

    public static User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getPhoneNumber(),
                null,
                userDto.getBirthday(),
                userDto.getGender(),
                null,
                null,
                null
        );
    }
}
