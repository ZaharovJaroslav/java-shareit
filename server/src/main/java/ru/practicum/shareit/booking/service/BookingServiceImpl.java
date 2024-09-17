package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingStatusMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OperationAccessException;
import ru.practicum.shareit.exception.TimeDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto addBooking(long bookerId, NewBookingRequestDto bookingRequest) {
        if (bookingRequest.getEnd().isBefore(bookingRequest.getStart())) {
            throw new TimeDataException(
                    "Дата завершения бронирования не может быть раньше даты начала бронирования");
        }
        User booker = userService.getUserById(bookerId);
        Item item = itemService.getItemById(bookingRequest.getItemId());
        User owner = userService.getUserById(item.getOwnerId());
        if (owner.getId() == bookerId) {
            throw new OperationAccessException("Арендодатель не может арендовать свой иснтрумент");
        }
        if (item.getAvailable()) {
            Booking booking = Booking.builder()
                    .start(bookingRequest.getStart())
                    .end(bookingRequest.getEnd())
                    .item(item)
                    .booker(booker)
                    .status(BookingStatus.WAITING)
                    .build();

            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new NotAvailableException("Инструмент уже в аренде");
        }
    }

    @Override
    public BookingDto findBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(("Бронирование с id =" + bookingId + "не найдено")));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwnerId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new ValidationException("Пользователь с id =" + userId + "не является владельцем");
        }
    }

    @Transactional
    @Override
    public BookingDto approve(long userId, long bookingId, Boolean approve) {
        BookingDto booking = findBookingById(userId, bookingId);
        Long ownerId = itemService.findOwnerId(booking.getItem().getId());
        if (ownerId.equals(userId)
                && booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new AlreadyExistsException("Бронирование уже одобрено владельцем инструмента");
        }
        if (!ownerId.equals(userId)) {
            throw new OperationAccessException("Пользователь с id = " + userId + "не являеться владельцем инстумента");
        }
        if (approve) {
            booking.setStatus(BookingStatus.APPROVED);
            bookingRepository.save(BookingStatus.APPROVED, bookingId);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(BookingStatus.REJECTED, bookingId);
        }

        return booking;
    }

    @Override
    public Collection<BookingDto> findAllByUserId(long userId, String state) {
        userService.getUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        BookingStatus bookingStatus = BookingStatusMapper.toBookingStatus(state);
        switch (bookingStatus) {
            case ALL:
                return BookingMapper.toBookingDto(bookingRepository.findByBookerIdOrderByStartDesc(userId));
            case CURRENT:
                return BookingMapper.toBookingDto(bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId, now, now));
            case PAST:
                return BookingMapper.toBookingDto(bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now));
            case FUTURE:
                return BookingMapper.toBookingDto(bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, now));
            case WAITING:
                BookingMapper.toBookingDto(bookingRepository.findByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(userId, now, BookingStatus.WAITING));
            case REJECTED:
                BookingMapper.toBookingDto(bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(userId, BookingStatus.REJECTED));
        }
        throw new BadRequestException("Введен некорректный запрос");
    }

    @Override
    public Collection<BookingDto> findAllBookingsByOwner(long ownerId, String state) {
        userService.getUserById(ownerId);
        LocalDateTime now = LocalDateTime.now();
        BookingStatus bookingStatus = BookingStatusMapper.toBookingStatus(state);
        switch (bookingStatus) {
            case ALL:
                return BookingMapper.toBookingDto(bookingRepository.findByItemOwnerId(ownerId));
            case CURRENT:
                return BookingMapper.toBookingDto(bookingRepository.findCurrentBookingsOwner(ownerId, now));
            case PAST:
                return BookingMapper.toBookingDto(bookingRepository.findPastBookingsOwner(ownerId, now));
            case FUTURE:
                return BookingMapper.toBookingDto(bookingRepository.findFutureBookingsOwner(ownerId, now));
            case WAITING:
                BookingMapper.toBookingDto(bookingRepository.findWaitingBookingsOwner(ownerId, now, BookingStatus.WAITING));
            case REJECTED:
                BookingMapper.toBookingDto(bookingRepository.findRejectedBookingsOwner((ownerId), BookingStatus.REJECTED));
        }
        throw new BadRequestException("Введен некорректный запрос");

    }
}
