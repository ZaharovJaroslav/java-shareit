package ru.practicum.shareit.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class UserDto {
    Long id;
    String name;
    String email;

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                    user.getName(),
                    user.getEmail()
        );
    }
}
