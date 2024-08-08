package ru.practicum.shareit.user;


import java.util.Collection;

interface UserService {
    Collection<User> getAllUsers();
    User addNewUser(User user);
    void deleteUserById(long userId);
    User UpdateUser(long userId, User user);
    User getUserById(long userId);

}
