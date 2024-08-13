package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.request.UpdateUserRequest;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Получение всех существующих пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@RequestBody User user) {
        log.debug("Добавлнеие нового пользователя: {}", user);
        if (user == null) {
            throw new NotFoundException("Не указан пользователь для добавления");
        }

        return userService.addNewUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        log.debug("Получени пользователя по id - {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        log.debug("Удаление пользователя по id - {}", userId);
        userService.deleteUserById(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable long userId, @RequestBody UpdateUserRequest request) {
        log.debug("Обновление данных существующего пользователя: {}", request);
        if (request == null) {
            throw new NotFoundException("Не указан пользователь для обновления");
        }

        return userService.updateUser(userId, request);
    }
}
