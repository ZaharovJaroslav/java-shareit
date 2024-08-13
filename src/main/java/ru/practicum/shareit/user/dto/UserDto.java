package ru.practicum.shareit.user.dto;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;

@Slf4j
@AllArgsConstructor
public class UserDto {
    Long id;
    String name;
    String email;

    public static UserDto toUserDto(User user) {
        log.debug("toUserDto({})", user);
        return new UserDto(user.getId(),
                    user.getName(),
                    user.getEmail()
        );
    }
}
