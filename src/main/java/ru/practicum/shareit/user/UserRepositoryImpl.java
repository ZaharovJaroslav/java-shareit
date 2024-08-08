package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository  {

   private final Map<Long,User> users;
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
            throw new NotFoundException("Нет существующих пользоватлей");
        }
        log.debug("Возвращен списко всех пользователей");
        return users.values();
    }

    @Override
    public User saveUser(User user) {
        log.debug("saveUser({})", user);
        generationNextId();
        user.setId(id);
        users.put(id, user);

        log.debug("Новый пользователь {} добавлен", user);
        return getUserById(id);
    }

    @Override
    public User getUserById(long userId) {
        log.debug("getUserById({})", userId);
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользвоателя с id - {} не существует");
        }

        log.debug("Пользоваетль возвращен");
        return users.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
       log.debug("deleteUserById({})", userId);
       getUserById(userId);
       users.remove(userId);
       log.debug("Пользователь удален");
    }

    @Override
    public User updateUser(User user) {
       log.debug("updateUser({})", user);
     //  users.put(user.getId(), user);

       log.debug("Пользователь {} обновлен",user);
       return getUserById(user.getId());
    }

    @Override
    public Optional<User> CheckEmailExist(String userEmail) {
       log.debug("CheckEmailExist({})", userEmail);
       return users.values().stream()
                .filter(user -> user.getEmail().equals(userEmail))
                .findFirst();
    }
}
