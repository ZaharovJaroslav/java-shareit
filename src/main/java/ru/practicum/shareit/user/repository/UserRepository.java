package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    /*Collection<User> getUsers();

    long saveUser(User user);

    void deleteUserById(long userId);

    void updateUser(User user);

    Optional<User> getUserById(long userId);

    Optional<User> checkEmailExist(String email);*/


}
