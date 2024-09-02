/*
package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

   private final Map<Long, User> users;
   private static long id;

   private UserRepositoryImpl() {
       this.users = new HashMap<>();
   }

    private void generationNextId() {
        log.debug("Генерация id для нового пользователя");
        ++id;
        log.debug("id = {} ", id);
    }

    @Override
    public Collection<User> getUsers() {
       log.debug("getUsers()");
        if (users.isEmpty()) {
            throw new NotFoundException("Нет существующих пользователей");
        }
        return users.values();
    }

    @Override
    public long saveUser(User user) {
        log.debug("saveUser({})", user);
        generationNextId();
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        log.debug("getUserById({})", userId);
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не существует");
        }
        return Optional.of(users.get(userId));
    }

    @Override
    public void deleteUserById(long userId) {
       log.debug("deleteUserById({})", userId);
       getUserById(userId);
       users.remove(userId);
    }

    @Override
    public void updateUser(User user) {
       log.debug("updateUser({})", user);
    }

    @Override
    public Optional<User> checkEmailExist(String userEmail) {
       log.debug("CheckEmailExist({})", userEmail);
       return users.values().stream()
                .filter(user -> user.getEmail().equals(userEmail))
                .findFirst();
    }
}
*/
