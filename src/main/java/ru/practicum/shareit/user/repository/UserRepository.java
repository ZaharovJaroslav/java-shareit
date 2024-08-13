package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> getUsers();

    User saveUser(User user);

    void deleteUserById(long userId);

    User updateUser(User user);

    User getUserById(long userId);

    Optional<User> checkEmailExist(String email);


}
