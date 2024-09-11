package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.request.UpdateUserRequest;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User addNewUser(User user);

    void deleteUserById(long userId);

    User updateUser(long userId, UpdateUserRequest request);

    User getUserById(long userId);

}
