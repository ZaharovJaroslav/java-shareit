package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;

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
        return repository.getUsers();
    }

    @Override
    public User getUserById(long userId) {
        log.debug("getUserById({})", userId);
        return repository.getUserById(userId);
    }

    @Override
    public User addNewUser(User user) {
        log.debug("addNewUser({})", user);
        validationUser(user);
        return repository.saveUser(user);

    }

    @Override
    public void deleteUserById(long userId) {
        log.debug("deleteUserById({})", userId);
        repository.deleteUserById(userId);
    }

    @Override
    public User UpdateUser(long userId, User user) {
        log.debug("UpdateUser({},{})", userId, user);
        User thisUser = getUserById(userId);

        if (user.getName() == null) {
            checkEmailExist(user.getEmail());
            thisUser.setEmail(user.getEmail());

        } else if (user.getEmail() == null) {
            thisUser.setName(user.getName());

        } else {
            checkEmailExist(user.getEmail());
            thisUser.setEmail(user.getEmail());
            thisUser.setName(user.getName());
        }

        return repository.updateUser(thisUser);
    }

    private void validationUser(User user) {
        log.info("Валидация пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не задано");
            throw new ValidationException("Имя пользователя не задано");
        }
        if (user.getEmail() == null|| user.getEmail().isBlank()) {
            log.warn("Не указана электронная почта пользоватлея");
            throw new ValidationException("Не указана электронная почта пользоватлея");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Электронная почта не содержит символ - @: {} ", user.getEmail());
            throw new ValidationException("Электронная почта не содержит символ - @");
        }
        checkEmailExist(user.getEmail());
    }

    private void checkEmailExist(String userEmail) {
        log.info("Проверка Электронной почты на существование: {}", userEmail);
        Optional<User> user =  repository.CheckEmailExist(userEmail);
        if (user.isPresent()) {
            log.warn("Пользователь с электронной почтой - {} уже зарегистрирован", user.get().getEmail());
            throw new ConflictException("Пользователь с электронной почтой уже зарегистрирован");
        }
    }
}
