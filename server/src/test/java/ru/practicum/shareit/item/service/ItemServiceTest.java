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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.UpdateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
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

}