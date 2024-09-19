package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;

    BookingDto bookingDtoForCreate;
    User owner1;
    User booker101;
    Item item1;
    ItemDto item;
    LocalDateTime now;
    LocalDateTime nowPlus10Hours;
    LocalDateTime nowPlus20Hours;

    @BeforeEach
    void setup() {
        now = LocalDateTime.now();
        nowPlus10Hours = LocalDateTime.now().plusHours(10);
        nowPlus20Hours = LocalDateTime.now().plusHours(20);

        booker101 = User.builder()
                .id(101L)
                .name("101 booker")
                .email("booker@pochta.tu")
                .build();

        bookingDtoForCreate = BookingDto.builder()
                .id(1L)
                .item(item)
                .start(nowPlus10Hours)
                .end(nowPlus20Hours)
                .status(BookingStatus.WAITING)
                .build();

        owner1 = User.builder()
                .id(1L)
                .name("imya usera 1 owner")
                .email("owner1@m.ri")
                .build();

        item1 = Item.builder()
                .id(1L)
                .name("nazvanie veschi 1")
                .description("opisanie veschi 1")
                .ownerId(owner1.getId())
                .available(true)
                .build();

    }

    @SneakyThrows
    @Test
    void add_whenAllIsOk_returnBookingForResponse() {
        BookingDto bookingDto1ForResponse =  BookingDto.builder()
                .id(1L)
                .start(bookingDtoForCreate.getStart())
                .end(bookingDtoForCreate.getEnd())
                .item(item)
                .booker(UserMapper.toUserDto(booker101))
                .status(BookingStatus.WAITING).build();

        when(bookingService.addBooking(anyLong(), any())).thenReturn(bookingDto1ForResponse);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker101.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoForCreate)))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto1ForResponse), result);
    }

    @SneakyThrows
    void updateByOwner() {
        BookingDto bookingDto1ForResponse =  BookingDto.builder()
                .id(1L)
                .start(bookingDtoForCreate.getStart())
                .end(bookingDtoForCreate.getEnd())
                .item(item)
                .booker(UserMapper.toUserDto(booker101))
                .status(BookingStatus.WAITING).build();

        when(bookingService.approve (anyLong(), anyLong(), any()))
                .thenReturn(bookingDto1ForResponse);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingDto1ForResponse.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", owner1.getId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto1ForResponse), result);
    }

    @SneakyThrows
    @Test
    void getWithStatusById() {
        BookingDto bookingDto1ForResponse =  BookingDto.builder()
                .id(1L)
                .start(bookingDtoForCreate.getStart())
                .end(bookingDtoForCreate.getEnd())
                .item(item)
                .booker(UserMapper.toUserDto(booker101))
                .status(BookingStatus.WAITING).build();

        when(bookingService.findBookingById (any(), any()))
                .thenReturn(bookingDto1ForResponse);
        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingDto1ForResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker101.getId()))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto1ForResponse), result);
    }

    @SneakyThrows
    @Test
    void getByUserId() {
        BookingDto bookingDto1ForResponse =  BookingDto.builder()
                .id(1L)
                .start(bookingDtoForCreate.getStart())
                .end(bookingDtoForCreate.getEnd())
                .item(item)
                .booker(UserMapper.toUserDto(booker101))
                .status(BookingStatus.WAITING).build();

        when(bookingService.findAllByUserId(anyLong(), any()))
                .thenReturn(List.of(bookingDto1ForResponse));

        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker101.getId())
                        .param("state", "ALL")

                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto1ForResponse)), result);
    }

    @SneakyThrows
    @Test
    void getByOwnerId() {
        BookingDto bookingDto1ForResponse =  BookingDto.builder()
                .id(1L)
                .start(bookingDtoForCreate.getStart())
                .end(bookingDtoForCreate.getEnd())
                .item(item)
                .booker(UserMapper.toUserDto(booker101))
                .status(BookingStatus.WAITING).build();

        when(bookingService.findAllBookingsByOwner  (anyLong(), any()))
                .thenReturn(List.of(bookingDto1ForResponse));

        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner1.getId())
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto1ForResponse)), result);
    }
}