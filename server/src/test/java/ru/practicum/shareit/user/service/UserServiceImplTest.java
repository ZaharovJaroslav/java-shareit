package ru.practicum.shareit.user.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
class UserServiceImplTest {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    User user1;
    UpdateUserRequest updateUserRequest;
    UserDto userDto;
    User user2;
    User userNull;
    UserDto userDtoAllFieldsNull;
    UserDto userDtoNull;
    User userAllFieldsNull;


    @BeforeEach
    void setUp() {
        user1 = new User(null, "name1", "email@emal.tr1");
        user2 = new User(null, "name2", "email@emal.tr2");
        updateUserRequest = new UpdateUserRequest("name_update", "email@emal.tr_update");

        userNull = null;
        userDto = UserDto.builder()
                .name("name userDto1")
                .email("userDto1@mans.gf")
                .build();

        userDtoAllFieldsNull = new UserDto();
        userDtoNull = null;
        userAllFieldsNull = new User();
    }

    @Test
    void getUserById_WhenAllIsOk() {
        User savedUser = userService.addNewUser(user1);
        User user = userService.getUserById(savedUser.getId());

        assertNotNull(user.getId());
        assertEquals(user.getName(), user1.getName());
        assertEquals(user.getEmail(), user1.getEmail());
    }

    @Test
    void getUserById_whenUserNotFoundInDb_return() {
        User savedUser = userService.addNewUser(user1);

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(9000L));
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        List<User> users = List.of(user1, user2);
        userService.addNewUser(user1);
        userService.addNewUser(user2);
        Collection<User> result = userService.getAllUsers();

        assertEquals(users.size(), result.size());
        for (User user : users) {
            assertThat(result, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(user.getName())),
                    hasProperty("email", equalTo(user.getEmail()))
            )));
        }
    }

    @Test
    void addToStorage() {
        userService.addNewUser(user1);
        Collection<User> users = userService.getAllUsers();
        boolean result = false;
        Long id = users.stream()
                .filter(u -> u.getEmail().equals(user1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);

        User userFromDb = userService.getUserById(id);

        assertEquals(1, users.size());
        assertEquals(user1.getName(), userFromDb.getName());
        assertEquals(user1.getEmail(), userFromDb.getEmail());
    }

    @Test
    void updateInStorage_whenAllIsOkAndNameIsNull_returnUpdatedUser() {
        User createdUser = userService.addNewUser(user1);

        Collection<User> beforeUpdateUsers = userService.getAllUsers();
        Long id = beforeUpdateUsers.stream()
                .filter(u -> u.getEmail().equals(user1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, createdUser.getId());

        User userFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getName(), user1.getName());
        assertEquals(userFromDbBeforeUpdate.getEmail(), user1.getEmail());

        userService.updateUser(createdUser.getId(), updateUserRequest);

        User userFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getId(), userFromDbAfterUpdate.getId());
        assertEquals(userFromDbAfterUpdate.getName(), updateUserRequest.getName());
        assertEquals(userFromDbAfterUpdate.getEmail(), updateUserRequest.getEmail());
    }

    @Test
    void updateInStorage_whenAllIsOk_returnUpdatedUser() {
        User createdUser = userService.addNewUser(user1);

        Collection<User> beforeUpdateUsers = userService.getAllUsers();
        Long id = beforeUpdateUsers.stream()
                .filter(u -> u.getEmail().equals(user1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, createdUser.getId());

        User userFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getName(), user1.getName());
        assertEquals(userFromDbBeforeUpdate.getEmail(), user1.getEmail());

        userService.updateUser(createdUser.getId(), updateUserRequest);

        User userFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getId(), userFromDbAfterUpdate.getId());
        assertEquals(userFromDbAfterUpdate.getName(), updateUserRequest.getName());
        assertEquals(userFromDbAfterUpdate.getEmail(), updateUserRequest.getEmail());
    }

    @Test
    void removeFromStorage() {
        User savedUser = userService.addNewUser(user1);
        Collection<User> beforeDelete = userService.getAllUsers();
        assertEquals(1, beforeDelete.size());
        userService.deleteUserById(savedUser.getId());
        Collection<User> afterDelete = userService.getAllUsers();
        assertEquals(0, afterDelete.size());
    }

    @Test
    void userMapperTest_mapToModel_whenAllIsOk() {
        User user1 = UserMapper.toUser(userDto);
        assertEquals(userDto.getId(), user1.getId());
        assertEquals(userDto.getName(), user1.getName());
        assertEquals(userDto.getEmail(), user1.getEmail());
    }

    @Test
    void userMapperTest_mapToModel_whenAllFieldsAreNull() {
        User userNull = UserMapper.toUser(userDtoAllFieldsNull);
        assertEquals(userDtoAllFieldsNull.getId(), userNull.getId());
        assertEquals(userDtoAllFieldsNull.getName(), userNull.getName());
        assertEquals(userDtoAllFieldsNull.getEmail(), userNull.getEmail());
    }

    @Test
    void userMapperTest_mapToDto_whenAllFieldsAreNull() {
        UserDto userDtoNull = UserMapper.toUserDto(userAllFieldsNull);
        assertEquals(userAllFieldsNull.getId(), userDtoNull.getId());
        assertEquals(userAllFieldsNull.getName(), userDtoNull.getName());
        assertEquals(userAllFieldsNull.getEmail(), userDtoNull.getEmail());
    }
}