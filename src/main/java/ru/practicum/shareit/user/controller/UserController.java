package ru.practicum.shareit.user;

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

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@RestController

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получение всех существующих пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@RequestBody User user) {
        log.info("Добавлнеие нового пользователя: {}", user);
        if (user == null) {
            throw new NotFoundException("Не указан пользователь для добавления");
        }
        return userService.addNewUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        log.info("Получени пользователя по id - {}", userId);
        return userService.getUserById(userId);

    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        log.info("Удаление пользователя");
        userService.deleteUserById(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable long userId, @RequestBody User user) {
        log.info("Обновление данных существующего пользователя: {}", user);
        if (user == null) {
            throw new NotFoundException("Не указан пользователь для обновления");
        }
        return userService.UpdateUser(userId, user);
    }





}
