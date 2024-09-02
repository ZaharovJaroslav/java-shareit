package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.request.UpdateUserRequest;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Collection<User> getAllUsers() {
        log.debug("getAllUsers()");
        return repository.findAll();
    }

    @Override
    public User getUserById(long userId) {
        log.debug("getUserById({})", userId);
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return user.get();
    }

    @Override
    public User addNewUser(User user) {
        log.debug("addNewUser({})", user);
        validationUser(user);
        repository.save(user);

        return user;
    }

    @Override
    public void deleteUserById(long userId) {
        log.debug("deleteUserById({})", userId);
        repository.deleteById(userId);
    }

    @Override
    public User updateUser(long userId, UpdateUserRequest request) {
        log.debug("UpdateUser({},{})", userId, request);
        /*    if (request.getEmail() != null) {
            checkEmailExist(request.getEmail());
        }*/
        User updatedUser = getUserById(userId);
        UserMapper.updateUserFields(updatedUser, request);

        return updatedUser;
    }

    private void validationUser(User user) {
        log.debug("Валидация пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не задано");
            throw new ValidationException("Имя пользователя не задано");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Не указана электронная почта пользователя");
            throw new ValidationException("Не указана электронная почта пользователя");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Электронная почта не содержит символ - @: {} ", user.getEmail());
            throw new ValidationException("Электронная почта не содержит символ - @");
        }
        //checkEmailExist(user.getEmail());
    }

/*    private void checkEmailExist(String userEmail) {
        log.debug("Проверка Электронной почты на существование: {}", userEmail);
        Optional<User> user =  repository.checkEmailExist(userEmail);
        if (user.isPresent()) {
            log.warn("Пользователь с электронной почтой - {} уже зарегистрирован", user.get().getEmail());
            throw new ConflictException("Пользователь с электронной почтой уже зарегистрирован");
        }
    }*/
}
