package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final User booker = User.builder()
            .name("Alex")
            .email("alex@mail.com")
            .build();

    private final User owner = User.builder()
            .name("Grey")
            .email("grey@mail.com")
            .build();

    private final Item item = Item.builder()
            .name("Предмет")
            .description("Описание предмета")
            .available(true)
            .ownerId(1L)
            .build();

    private final Booking booking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusHours(1L))
            .end(LocalDateTime.now().plusDays(1L))
            .build();

    private final Booking pastBooking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusDays(2L))
            .end(LocalDateTime.now().minusDays(1L))
            .build();

    private final Booking futureBooking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().plusDays(1L))
            .end(LocalDateTime.now().plusDays(2L))
            .build();

    private final Booking currentBooking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusHours(1L))
            .end(LocalDateTime.now().plusDays(1L))
            .build();

    @BeforeEach
    public void init() {
        testEntityManager.persist(booker);
        testEntityManager.persist(owner);
        testEntityManager.persist(item);
        testEntityManager.flush();
        bookingRepository.save(booking);
        bookingRepository.save(pastBooking);
        bookingRepository.save(futureBooking);
    }

    @AfterEach
    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    @Test
    void findByBookerIdOrderByStartDesc() {
        Collection<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(booker.getId());
        assertEquals(bookings.size(), 3);
    }

    @Test
    void findByBookerIdAndEndIsBeforeOrderByStartDesc() {
        Collection<Booking> bookings = bookingRepository
                .findByBookerIdAndEndIsBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now());
        assertEquals(bookings.size(), 1);
    }

    @Test
    void findByBookerIdAndStatusIsOrderByStartDesc() {
        Collection<Booking> bookings = bookingRepository
                .findByBookerIdAndStatusIsOrderByStartDesc(booker.getId(), BookingStatus.APPROVED);
        assertEquals(bookings.size(), 3);

    }

    @Test
    void findByBooker_findByBookerIdAndStartIsAfterOrderByStartDesc() {
        Collection<Booking> bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(booker.getId(), LocalDateTime.now());
        assertEquals(1, bookings.size());
    }

    @Test
    void findByBooker_findByBookerIdAndEndIsBeforeOrderByStartDesc() {
        Collection<Booking> bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now());
        assertEquals(1, bookings.size());
    }

    @Test
    void findByBooker_findByItemOwnerId() {
        Collection<Booking> bookings = bookingRepository.findByItemOwnerId(item.getOwnerId());
        assertEquals(3, bookings.size());
    }

    @Test
    void findByBooker_findBookingsItem() {
        Collection<Booking> bookings = bookingRepository.findBookingsItem(item.getId());
        assertEquals(3, bookings.size());
    }




}