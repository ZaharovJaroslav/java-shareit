package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

interface UserRepository {
    Collection<User> getUsers();
    User saveUser(User user);
    void deleteUserById(long userId);
    User updateUser(User user);

    User getUserById(long userId);
    Optional<User> CheckEmailExist(String email);


}
