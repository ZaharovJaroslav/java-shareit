package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRequestMapperImplTest {

    @Test
    void testToItemRequestDto_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@mail.com");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Описание предмета");
        itemRequest.setCreator(user);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }

    @Test
    void testToItemRequestResponseDto_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@mail.com");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Описание предмета");
        itemRequest.setCreator(user);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getCreator().getId(), result.getRequestor().getId());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }
}