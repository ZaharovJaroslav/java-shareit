package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.request.UpdateUserRequest;


@Slf4j
@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return  new UserDto(user.getId(),
                            user.getName(),
                            user.getEmail());
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        log.debug("updateUserFields({}, {})", user, request);
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasName()) {
            user.setName(request.getName());
        }
        return user;
    }
}
