package ru.practicum.shareit.item.service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.UpdateItemRequestDto;

import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;
    ItemRequest itemRequest1;
    UserDto ownerDto1;
    User owner1;
    UserDto requesterDto101;
    User requester101;
    UserDto bookerDto;
    User booker;
    UserDto userDtoForTest;
    User userForTest;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    ItemDto itemDto1;
    ItemRequestDto itemRequestDto1;
    CommentDto commentDto;

    Booking bookingFromBd;
    TypedQuery<Item> query;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlus10min = now.plusMinutes(10);
        nowPlus10hours = now.plusHours(10);

        ownerDto1 = UserDto.builder()
                .name("name ownerDto1")
                .email("ownerDto1@mans.gf")
                .build();

        owner1 = User.builder()
                .id(ownerDto1.getId())
                .name(ownerDto1.getName())
                .email(ownerDto1.getEmail())
                .build();

        requesterDto101 = UserDto.builder()
                .name("name requesterDto101")
                .email("requesterDto101@mans.gf")
                .build();

        requester101 = User.builder()
                .id(requesterDto101.getId())
                .name(requesterDto101.getName())
                .email(requesterDto101.getEmail())
                .build();

        userDtoForTest = UserDto.builder()
                .name("name userDtoForTest")
                .email("userDtoForTest@userDtoForTest.zx")
                .build();

        userForTest = User.builder()
                .name(userDtoForTest.getName())
                .email(userDtoForTest.getEmail())
                .build();

        bookerDto = UserDto.builder()
                .name("booker")
                .email("booker@wa.dzd")
                .build();

        booker = User.builder()
                .name(bookerDto.getName())
                .email(bookerDto.getEmail())
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

        itemDto1 = ItemDto.builder()
                .name(item1.getName())
                .description(item1.getDescription())
                .available(item1.getAvailable())
                .build();

        itemRequestDto1 = ItemRequestDto.builder()
                .description(item1.getDescription())
                .requestor(UserMapper.toUserDto(requester101))
                .created(now)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .created(now)
                .text("comment 1")
                .authorName(userForTest.getName())
                .build();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addItem_whenAllAreOk_returnSavedItemDto() {
        User savedOwnerDto1 = userService.addNewUser(owner1);

        //Before save.
        query =
                em.createQuery("Select i from Item i", Item.class);
        List<Item> beforeSave = query.getResultList();

        assertEquals(0, beforeSave.size());

        //After save.
        ItemDto savedItemDto = itemService.addNewItem(savedOwnerDto1.getId(), itemDto1);
        List<Item> afterSave = query.getResultList();

        assertEquals(1, afterSave.size());
        assertEquals(savedItemDto.getId(), afterSave.get(0).getId());
        assertEquals(savedItemDto.getRequestId(), afterSave.get(0).getItemRequest());
        assertEquals(savedItemDto.getDescription(), afterSave.get(0).getDescription());
        assertEquals(savedItemDto.getName(), afterSave.get(0).getName());
    }

    @Test
    void addItemRequest_whenRequesterIdIsNull_returnNotFoundRecordInBD() {
        Long requesterId = 1001L;
        assertThrows(NotFoundException.class,
                () -> itemRequestService.addNewItemRequest(itemRequestDto1, requesterId));
    }

    @Test
    void addItem_whenUserNotFound_returnNotFoundException() {
        assertThrows(NotFoundException.class, () -> itemService.addNewItem(10000L, itemDto1));
    }


    @Test
    void getItemsByUserId_whenOk_returnItemDtoList() {
        User savedOwnerDto1 = userService.addNewUser(owner1);
        ItemDto savedItemDto = itemService.addNewItem(savedOwnerDto1.getId(), itemDto1);
        Collection<ItemDto> collection = itemService.getAllItemsUser(savedOwnerDto1.getId());
        List<ItemDto> itemDtos = collection.stream().toList();


        assertEquals(1, itemDtos.size());
        assertEquals(savedItemDto.getId(), itemDtos.get(0).getId());
        assertEquals(savedItemDto.getName(), itemDtos.get(0).getName());
        assertEquals(savedItemDto.getDescription(), itemDtos.get(0).getDescription());
        assertEquals(savedItemDto.getRequestId(), itemDtos.get(0).getRequestId());
        assertEquals(savedItemDto.getAvailable(), itemDtos.get(0).getAvailable());
    }

    @Test
    void getItemsByUserId_whenUserNotFoundInBD_returnException() {
        assertThrows(NotFoundException.class, () -> itemService.getAllItemsUser(1000L));
    }

    @Test
    void updateInStorage_whenAllIsOk_returnItemFromDb() {
        User savedOwnerDto1 = userService.addNewUser(owner1);
        ItemDto savedItemDtoBeforeUpd = itemService.addNewItem(savedOwnerDto1.getId(), itemDto1);

        Collection<ItemDto> collection = itemService.getAllItemsUser(savedOwnerDto1.getId());
        List<ItemDto> itemDtos = collection.stream().toList();

        assertEquals(1, itemDtos.size());
        assertEquals(savedItemDtoBeforeUpd.getId(), itemDtos.get(0).getId());
        assertEquals(savedItemDtoBeforeUpd.getName(), itemDtos.get(0).getName());
        assertEquals(savedItemDtoBeforeUpd.getDescription(), itemDtos.get(0).getDescription());
        assertEquals(savedItemDtoBeforeUpd.getRequestId(), itemDtos.get(0).getRequestId());
        assertEquals(savedItemDtoBeforeUpd.getAvailable(), itemDtos.get(0).getAvailable());

        UpdateItemRequestDto updatedItem = UpdateItemRequestDto.builder()
                .name("new name")
                .description("new description")
                .available(true)
                .build();

        ItemDto savedUpdItem =
                itemService.updateItem(savedItemDtoBeforeUpd.getId(), savedOwnerDto1.getId(), updatedItem);

        assertNotEquals(savedItemDtoBeforeUpd.getName(), savedUpdItem.getName());
        assertNotEquals(savedItemDtoBeforeUpd.getDescription(), savedUpdItem.getDescription());
        assertEquals(savedItemDtoBeforeUpd.getId(), savedUpdItem.getId());
        assertEquals(savedItemDtoBeforeUpd.getAvailable(), savedUpdItem.getAvailable());
    }

    @Test
    void getItemById_whenOk_returnItemFromDb() {
        User savedOwnerDto1 = userService.addNewUser(owner1);
        ItemDto savedItemDtoBeforeUpd = itemService.addNewItem(savedOwnerDto1.getId(), itemDto1);
        Item itemDtoFromBd = itemService.getItemById(savedItemDtoBeforeUpd.getId());

        assertEquals(savedItemDtoBeforeUpd.getId(), itemDtoFromBd.getId());
        assertEquals(savedItemDtoBeforeUpd.getName(), itemDtoFromBd.getName());
        assertEquals(savedItemDtoBeforeUpd.getDescription(), itemDtoFromBd.getDescription());

        assertEquals(savedItemDtoBeforeUpd.getAvailable(), itemDtoFromBd.getAvailable());
    }

    @Test
    void testSearchItemsByText() {
        User savedOwnerDto1 = userService.addNewUser(owner1);
        ItemDto savedItemDto01 = itemService.addNewItem(savedOwnerDto1.getId(), itemDto1);

        User savedRequester = userService.addNewUser(requester101);
        ItemDto itemDto02 = itemDto1.toBuilder().name("new item").description("new description").build();

        ItemDto savedItemDto02 = itemService.addNewItem(savedOwnerDto1.getId(), itemDto02);

        Collection<ItemDto> collection = itemService.findItemByNameOrDescription("nEw");
        List<ItemDto> itemDtoList = collection.stream().toList();

        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());
        assertEquals(itemDto02.getDescription(), itemDtoList.stream().findFirst().get().getDescription());
    }

    @Test
    void commentToDto_whenCommentIsOk_returnCommentDto() {
        Comment comment = Comment.builder()
                .id(0L)
                .author(booker)
                .created(now)
                .text("comment")
                .item(item1).build();
        CommentDto commentDto1 = CommentMapper.toDto(comment);
        assertEquals(comment.getId(), commentDto1.getId());
        assertEquals(comment.getText(), commentDto1.getText());
        assertEquals(comment.getAuthor().getName(), commentDto1.getAuthorName());
        assertEquals(comment.getCreated(), commentDto1.getCreated());
    }

    @Test
    void saveComment_whenUserNotFound_thenReturnNotFoundRecordInBD() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("comment 1")
                .authorName("name user for test 2")
                .created(now.minusDays(5))
                .build();

        assertThrows(NotFoundException.class, () -> itemService.addComment(1000L, 1L, commentDto));
    }

    @Test
    void saveComment_whenItemNotFound_thenReturnNotFoundRecordInDb() {
        User savedUser1 = userService.addNewUser(owner1);
        User savedUser2 = userService.addNewUser(userForTest);
        CommentDto commentDto = CommentDto.builder()
                .authorName(savedUser2.getName())
                .authorName("comment from user 1")
                .created(now)
                .build();
        Long notFoundItemId = 1001L;
        //Пользователь не арендовал эту вещь.
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.addComment(notFoundItemId, savedUser1.getId(), commentDto));
        assertEquals("Инструмента id = 1001не существует", ex.getMessage());
    }

    @Test
    void saveComment_whenAllAreOk_thenReturnComment() {
        CommentDto inputCommentDto = CommentDto.builder().id(1L).text("new comment for test").build();

        User owner2 = User.builder()
                .id(2L)
                .name("name for owner")
                .email("owner2@aadmf.wreew")
                .build();

        User userForTest2 = User.builder()
                .id(1L)
                .name("name user for test 2")
                .email("userForTest2@ahd.ew")
                .build();

        Item zaglushka = Item.builder().id(1L).name("zaglushka").description("desc item zaglushka")
                .ownerId(owner2.getId()).build();

        Booking bookingFromBd = Booking.builder()
                .id(1L)
                .item(zaglushka)
                .booker(userForTest2)
                .start(now.minusDays(10))
                .end(now.minusDays(5))
                .build();///
        List<Booking> list = new ArrayList<>();
        list.add(bookingFromBd);

        Item itemFromBd = Item.builder()
                .id(1L)
                .name("name for item")
                .description("desc for item")
                .ownerId(owner2.getId())
                .available(true)
                .build();///

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("comment 1")
                .authorName("name user for test 2")
                .created(now.minusDays(5))
                .build();

        Comment outputComment = Comment.builder()
                .id(1L)
                .author(userForTest2)
                .text("comment 1")
                .item(itemFromBd)
                .build();

        UserService userService1 = mock(UserService.class);
        ItemRepository itemRepository = mock(ItemRepository.class);
        CommentRepository commentRepository2 = mock(CommentRepository.class);
        BookingRepository bookingRepository2 = mock(BookingRepository.class);
        ItemRequestService itemRequestService = mock(ItemRequestService.class);

        ItemService itemService2 = new ItemServiceImpl(itemRepository, userService1, bookingRepository2,commentRepository2,itemRequestService);

        when(userService1.getUserById(anyLong()))
                .thenReturn(userForTest2);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemFromBd));
        when(commentRepository2.save(any()))
                .thenReturn(outputComment);
        when(bookingRepository2.findByItemIdAndBookerIdAndStatusIsAndEndIsBefore(any(),any(),any(),any())).thenReturn(list);

        CommentDto outputCommentDto =
                itemService2.addComment(itemFromBd.getId(), userForTest2.getId(), inputCommentDto);

        assertEquals(commentDto.getText(), outputCommentDto.getText());
        assertEquals(commentDto.getAuthorName(), outputCommentDto.getAuthorName());
        assertEquals(commentDto.getId(), outputCommentDto.getId());
        assertNotEquals(commentDto.getCreated(), outputCommentDto.getCreated());
    }

}