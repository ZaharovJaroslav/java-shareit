package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.request.UpdateUserRequest;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

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
