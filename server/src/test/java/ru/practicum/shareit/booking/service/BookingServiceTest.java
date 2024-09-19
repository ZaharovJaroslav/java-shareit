package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
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
class BookingServiceTest {

    private UserService userService;
    private ItemService itemService;
    private BookingService bookingService;
    private BookingRepository bookingRepository;

    User user;
    User owner;
    Item item;
    BookingDto bookingDto;
    Booking booking;
    BookingDto currentBookingDto;
    Booking currentBooking;
    //Past
    BookingDto pastBookingDto;
    Booking pastBooking;
    BookingDto futureBookingDto;
    Booking futureBooking;
    BookingDto waitingBookingDto;
    Booking waitingBooking;
    BookingDto rejectedBookingDto;
    Booking rejectedBooking;
    NewBookingRequestDto newBookingRequestDto;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemService = mock(ItemService.class);
        userService = mock(UserService.class);
        bookingService = new BookingServiceImpl(bookingRepository, userService, itemService);

        LocalDateTime now = LocalDateTime.now();

        user = User.builder()
                .id(1L)
                .name("name user 1")
                .email("user1@ugvg@rsdx")
                .build();

        owner = User.builder()
                .id(2L)
                .name("name owner 2")
                .email("owner@jjgv.zw")
                .build();

        item = Item.builder()
                .id(1L)
                .name("name item 1")
                .description("desc item 1")
                .ownerId(2L)
                .available(true)
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .item(ItemMapper.toItemDto(item))
                .booker(UserMapper.toUserDto(user))
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .status(BookingStatus.WAITING)
                .build();

        newBookingRequestDto = NewBookingRequestDto.builder()
                .itemId(item.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();


        booking = Booking.builder()
                .id(bookingDto.getId())
                .item(item)
                .booker(user)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();

        currentBookingDto = bookingDto.toBuilder()
                .id(2L)
                .start(now.minusDays(1))
                .end(now.plusDays(1))
                .status(BookingStatus.APPROVED)
                .build();

        currentBooking = Booking.builder()
                .id(currentBookingDto.getId())
                .item(item)
                .booker(user)
                .start(currentBookingDto.getStart())
                .end(currentBookingDto.getEnd())
                .status(currentBookingDto.getStatus())
                .build();

        pastBookingDto = bookingDto.toBuilder()
                .id(3L)
                .start(now.minusDays(1000))
                .end(now.minusDays(999))
                .status(BookingStatus.APPROVED)
                .build();

        pastBooking = Booking.builder()
                .id(pastBookingDto.getId())
                .item(item)
                .booker(user)
                .start(pastBookingDto.getStart())
                .end(pastBookingDto.getEnd())
                .status(pastBookingDto.getStatus())
                .build();

        futureBookingDto = bookingDto.toBuilder()
                .id(4L)
                .start(now.minusDays(999))
                .end(now.minusDays(1000))
                .status(BookingStatus.APPROVED)
                .build();

        futureBooking = Booking.builder()
                .id(futureBookingDto.getId())
                .item(item)
                .booker(user)
                .start(futureBookingDto.getStart())
                .end(futureBookingDto.getEnd())
                .status(futureBookingDto.getStatus())
                .build();

        waitingBookingDto = bookingDto.toBuilder()
                .id(5L)
                .start(now.plusDays(1))
                .end(now.minusDays(2))
                .status(BookingStatus.WAITING)
                .build();

        waitingBooking = Booking.builder()
                .id(waitingBookingDto.getId())
                .item(item)
                .booker(user)
                .start(waitingBookingDto.getStart())
                .end(waitingBookingDto.getEnd())
                .status(waitingBookingDto.getStatus())
                .build();

        rejectedBookingDto = bookingDto.toBuilder()
                .id(6L)
                .start(now.plusDays(100))
                .end(now.plusDays(101))
                .status(BookingStatus.REJECTED)
                .build();

        rejectedBooking = Booking.builder()
                .id(rejectedBookingDto.getId())
                .item(item)
                .booker(user)
                .start(rejectedBookingDto.getStart())
                .end(rejectedBookingDto.getEnd())
                .status(rejectedBookingDto.getStatus())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createBooking_whenAllAreOk_returnSavedBookingDto() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(itemService.getItemById(anyLong())).thenReturn(item);
        when(userService.getUserById(anyLong())).thenReturn(owner);

        when(bookingRepository.save(any())).thenReturn(booking);
        BookingDto savedBookingForResponse = bookingService.addBooking(user.getId(), newBookingRequestDto);

        assertNotNull(savedBookingForResponse);
        assertEquals(newBookingRequestDto.getStart(), savedBookingForResponse.getStart());
        assertEquals(newBookingRequestDto.getEnd(), savedBookingForResponse.getEnd());
        assertEquals(newBookingRequestDto.getItemId(), savedBookingForResponse.getItem().getId());


    }

    @Test
    void getWithStatusById_whenRequestByOwnerOrBooker_returnBookingForResponse() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        BookingDto outputBooking = bookingService.findBookingById(owner.getId(), booking.getId());

        assertEquals(booking.getId(), outputBooking.getId());
        assertEquals(booking.getBooker().getId(), outputBooking.getBooker().getId());
        assertEquals(booking.getItem().getName(), outputBooking.getItem().getName());
        assertEquals(booking.getStart(), outputBooking.getStart());
        assertEquals(booking.getEnd(), outputBooking.getEnd());
        assertEquals(booking.getStatus(), outputBooking.getStatus());
    }

    @Test
    void getWithStatusById_whenRequestByWrongUser_returnBookingForResponse() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.findBookingById(1000L, booking.getId()));
    }

    @Test
    void getByUserId_whenStateIsAll_returnAllBookings() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong()))
                .thenReturn(List.of(booking));
        Collection<BookingDto> collection = bookingService.findAllByUserId(user.getId(), "ALL");
        List<BookingDto> result = collection.stream().toList();


        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByUserId_whenStateIsCurrent_returnAllCurrentBookings() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(currentBooking));
        Collection<BookingDto> collection = bookingService.findAllByUserId(user.getId(), "CURRENT");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(currentBooking.getId(), result.get(0).getId());
        assertEquals(currentBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(currentBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(currentBooking.getStart(), result.get(0).getStart());
        assertEquals(currentBooking.getEnd(), result.get(0).getEnd());
        assertEquals(currentBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByUserId_whenStateIsPast_returnAllPastBookings() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(pastBooking));
        Collection<BookingDto> collection = bookingService.findAllByUserId(user.getId(), "PAST");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.get(0).getId());
        assertEquals(pastBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(pastBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(pastBooking.getStart(), result.get(0).getStart());
        assertEquals(pastBooking.getEnd(), result.get(0).getEnd());
        assertEquals(pastBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByUserId_whenStateIsFuture_returnAllFutureBookings() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(futureBooking));
        Collection<BookingDto> collection = bookingService.findAllByUserId(user.getId(), "FUTURE");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(futureBooking.getId(), result.get(0).getId());
        assertEquals(futureBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(futureBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(futureBooking.getStart(), result.get(0).getStart());
        assertEquals(futureBooking.getEnd(), result.get(0).getEnd());
        assertEquals(futureBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByUserId_whenStateIsWaiting_returnAllWaitingBookings() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(waitingBooking));
        Collection<BookingDto> collection = bookingService.findAllByUserId(user.getId(), "WAITING");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(waitingBooking.getId(), result.get(0).getId());
        assertEquals(waitingBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(waitingBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(waitingBooking.getStart(), result.get(0).getStart());
        assertEquals(waitingBooking.getEnd(), result.get(0).getEnd());
        assertEquals(waitingBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByUserId_whenStateIsRejected_returnAllRejectedBookings() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(rejectedBooking));
        Collection<BookingDto> collection = bookingService.findAllByUserId(user.getId(), "REJECTED");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(rejectedBooking.getId(), result.get(0).getId());
        assertEquals(rejectedBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(rejectedBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(rejectedBooking.getStart(), result.get(0).getStart());
        assertEquals(rejectedBooking.getEnd(), result.get(0).getEnd());
        assertEquals(rejectedBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByOwnerId_whenStateIsAll_returnAllBookings() {
        when(userService.getUserById(anyLong())).thenReturn(owner);
        when(bookingRepository.findByItemOwnerId(anyLong()))
                .thenReturn(List.of(booking));
        Collection<BookingDto> collection = bookingService.findAllBookingsByOwner(owner.getId(), "ALL");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        assertEquals(booking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(booking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(booking.getStart(), result.get(0).getStart());
        assertEquals(booking.getEnd(), result.get(0).getEnd());
        assertEquals(booking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByOwnerId_whenStateIsCurrent_returnAllCurrentBookings() {
        when(userService.getUserById(anyLong())).thenReturn(owner);
        when(bookingRepository.findCurrentBookingsOwner(anyLong(), any()))
                .thenReturn(List.of(currentBooking));
        Collection<BookingDto> collection = bookingService.findAllBookingsByOwner(owner.getId(), "CURRENT");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(currentBooking.getId(), result.get(0).getId());
        assertEquals(currentBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(currentBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(currentBooking.getStart(), result.get(0).getStart());
        assertEquals(currentBooking.getEnd(), result.get(0).getEnd());
        assertEquals(currentBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByOwnerId_whenStateIsPast_returnAllPastBookings() {

      when(userService.getUserById(anyLong())).thenReturn(owner);
        when(bookingRepository.findPastBookingsOwner(anyLong(), any()))
                .thenReturn(List.of(pastBooking));
        Collection<BookingDto> collection = bookingService.findAllBookingsByOwner(owner.getId(), "PAST");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.get(0).getId());
        assertEquals(pastBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(pastBooking.getItem().getName(), result.get(0).getItem().getName());
         assertEquals(pastBooking.getStart(), result.get(0).getStart());
        assertEquals(pastBooking.getEnd(), result.get(0).getEnd());
        assertEquals(pastBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByOwnerId_whenStateIsFuture_returnAllFutureBookings() {

        when(userService.getUserById(anyLong())).thenReturn(owner);
        when(bookingRepository.findFutureBookingsOwner(anyLong(), any()))
                .thenReturn(List.of(futureBooking));
       Collection<BookingDto> collection = bookingService.findAllBookingsByOwner(owner.getId(), "FUTURE");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(futureBooking.getId(), result.get(0).getId());
        assertEquals(futureBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(futureBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(futureBooking.getStart(), result.get(0).getStart());
        assertEquals(futureBooking.getEnd(), result.get(0).getEnd());
        assertEquals(futureBooking.getStatus(), result.get(0).getStatus());
    }


    @Test
    void getByOwnerId_whenStateIsWaiting_returnAllWaitingBookings() {
        when(userService.getUserById(anyLong())).thenReturn(owner);
        when(bookingRepository.findWaitingBookingsOwner(anyLong(), any(), any()))
                .thenReturn(List.of(waitingBooking));
        Collection<BookingDto> collection = bookingService.findAllBookingsByOwner(owner.getId(), "WAITING");
        List<BookingDto> result = collection.stream().toList();

        assertEquals(1, result.size());
        assertEquals(waitingBooking.getId(), result.get(0).getId());
        assertEquals(waitingBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(waitingBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(waitingBooking.getStart(), result.get(0).getStart());
        assertEquals(waitingBooking.getEnd(), result.get(0).getEnd());
        assertEquals(waitingBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByOwnerId_whenStateIsRejected_returnAllRejectedBookings() {
        when(userService.getUserById(anyLong())).thenReturn(owner);
        when(bookingRepository.findRejectedBookingsOwner(anyLong(), any()))
                .thenReturn(List.of(rejectedBooking));
      Collection<BookingDto> collection = bookingService.findAllBookingsByOwner(owner.getId(), "REJECTED");
        List<BookingDto> result = collection.stream().toList();
        assertEquals(1, result.size());
        assertEquals(rejectedBooking.getId(), result.get(0).getId());
        assertEquals(rejectedBooking.getBooker().getId(), result.get(0).getBooker().getId());
        assertEquals(rejectedBooking.getItem().getName(), result.get(0).getItem().getName());
        assertEquals(rejectedBooking.getStart(), result.get(0).getStart());
        assertEquals(rejectedBooking.getEnd(), result.get(0).getEnd());
        assertEquals(rejectedBooking.getStatus(), result.get(0).getStatus());
    }
}