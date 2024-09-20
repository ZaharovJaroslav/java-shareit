package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;
    ItemRequest itemRequest1;
    UserDto ownerDto1;
    UserDto requesterDto101;
    User owner1;
    User requester101;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    ItemRequestDto itemRequestDto1;
    TypedQuery<ItemRequest> query;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlus10min = now.plusMinutes(10);
        nowPlus10hours = now.plusHours(10);

        ownerDto1 = UserDto.builder()
                .name("name userDto1")
                .email("userDto1@mans.gf")
                .build();
        requesterDto101 = UserDto.builder()
                .name("name userDto2")
                .email("userDto2@mans.gf")
                .build();

        owner1 = User.builder()
                .id(ownerDto1.getId())
                .name(ownerDto1.getName())
                .email(ownerDto1.getEmail())
                .build();

        requester101 = User.builder()
                .id(requesterDto101.getId())
                .name(requesterDto101.getName())
                .email(requesterDto101.getEmail())
                .build();

        itemRequest1 = ItemRequest.builder()
                .description("description for request 1")
                .creator(requester101)
                .created(now)
                .build();

        item1 = Item.builder()
                .name("name for item 1")
                .description("description for item 1")
                .ownerId(owner1.getId())
                .available(true)
                .build();

        itemRequestDto1 = ItemRequestDto.builder()
                .description(item1.getDescription())
                .requestor(requesterDto101)
                .created(now)
                .build();
    }

    @Test
    void addItemRequest() {
        //Before save.
        User savedOwnerDto1 = userService.addNewUser(owner1);
        query =
                em.createQuery("Select ir from ItemRequest ir", ItemRequest.class);
        List<ItemRequest> beforeSave = query.getResultList();

        assertEquals(0, beforeSave.size());

        //After save.
        ItemRequestDto savedItemRequest =
                itemRequestService.addNewItemRequest(itemRequestDto1, savedOwnerDto1.getId());
        List<ItemRequest> afterSave = query.getResultList();

        assertEquals(1, afterSave.size());
        assertEquals(savedItemRequest.getId(), afterSave.get(0).getId());
        assertEquals(savedItemRequest.getCreated(), afterSave.get(0).getCreated());
        assertEquals(savedItemRequest.getDescription(), afterSave.get(0).getDescription());
    }


    @Test
    void addItemRequest_whenRequesterIdIsNull_returnNotFoundRecordInBD() {
        Long requesterId = 1001L;
        assertThrows(NotFoundException.class,
                () -> itemRequestService.addNewItemRequest(itemRequestDto1, requesterId));
    }

    @Test
    void getItemRequestsByUserId() {
        User savedUserDto = userService.addNewUser(requester101);
        ItemRequestDto savedItemRequest =
                itemRequestService.addNewItemRequest(itemRequestDto1, savedUserDto.getId());

        query = em.createQuery("Select ir from ItemRequest ir", ItemRequest.class);

        Collection<ItemRequestDto> collection =
                itemRequestService.findUserRequests(savedUserDto.getId());
        List<ItemRequestDto> itemsFromDb = collection.stream().toList();

        assertEquals(1, itemsFromDb.size());

        assertEquals(savedItemRequest.getId(), itemsFromDb.get(0).getId());
        assertEquals(savedItemRequest.getRequestor().getId(), itemsFromDb.get(0).getRequestor().getId());
        assertEquals(savedItemRequest.getRequestor().getName(), itemsFromDb.get(0).getRequestor().getName());
        assertEquals(savedItemRequest.getCreated(), itemsFromDb.get(0).getCreated());
        assertEquals(itemRequestDto1.getDescription(), itemsFromDb.get(0).getDescription());
    }

    @Test
    void getItemRequestsByUserId_whenUserNotFound_returnNotFoundRecordInDb() {
        Long requesterId = 1001L;
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemRequestService.findUserRequests(requesterId));
        assertEquals(("Пользователя с таким id не существует"), ex.getMessage());
    }

    @Test
    void getAllRequestForSee_whenRequesterNotFound_returnNotFoundRecordInDb() {
        Long requesterId = 1001L;
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemRequestService.findUserRequests(requesterId));
        assertEquals(("Пользователя с таким id не существует"), ex.getMessage());
    }


    @Test
    void getItemRequestById_whenAllIsOk_returnItemRequestDtoWithAnswers() {
        User savedRequesterDto = userService.addNewUser(requester101);
        User savedOwnerDto = userService.addNewUser(owner1);
        User observer = userService.addNewUser(User.builder().name("nablyudatel").email("1@re.hg").build());

        ItemRequestDto savedItRequest =
                itemRequestService.addNewItemRequest(itemRequestDto1, savedRequesterDto.getId());

        //Для юзера 1.
        ItemRequestDto itRequestDtoFromDbObserver =
                itemRequestService.findById(observer.getId(), savedItRequest.getId());

        assertEquals(savedItRequest.getId(), itRequestDtoFromDbObserver.getId());
        assertEquals(savedItRequest.getCreated(), itRequestDtoFromDbObserver.getCreated());
        assertEquals(savedItRequest.getDescription(), itRequestDtoFromDbObserver.getDescription());
        //Для юзера 2.
        ItemRequestDto itemRequestDtoWithAnswerForOwner =
                itemRequestService.findById(savedOwnerDto.getId(), savedItRequest.getId());

        assertEquals(savedItRequest.getId(), itemRequestDtoWithAnswerForOwner.getId());
        assertEquals(savedItRequest.getCreated(), itemRequestDtoWithAnswerForOwner.getCreated());
        assertEquals(savedItRequest.getDescription(), itemRequestDtoWithAnswerForOwner.getDescription());
        //Дл юзера 3.
        ItemRequestDto itReqDtoWithAnswerForRequester =
                itemRequestService.findById(savedRequesterDto.getId(), savedItRequest.getId());

        assertEquals(savedItRequest.getId(), itReqDtoWithAnswerForRequester.getId());
        assertEquals(savedItRequest.getCreated(), itReqDtoWithAnswerForRequester.getCreated());
        assertEquals(savedItRequest.getDescription(), itReqDtoWithAnswerForRequester.getDescription());
    }

    @Test
    void getItemRequestById_whenRequestNotFound_returnNotFoundRecordInBD() {
        User savedRequesterDto = userService.addNewUser(requester101);
        Long requestId = 1001L;
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemRequestService.findById(savedRequesterDto.getId(), requestId));
        assertEquals("Запрос на аренду с id = 1001 не найден",
                ex.getMessage());
    }

    @Test
    void getItemRequestById_whenUserNotFound_returnNotFoundRecordInBD() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemRequestService.findById(1001L, 1L));
        assertEquals("Запрос на аренду с id = 1 не найден",
                ex.getMessage());
    }
}