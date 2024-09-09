package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.request.NewBookingRequest;

import java.util.Collection;

public interface BookingService {
    BookingDto addBooking(long userId, NewBookingRequest bookingRequest);
    BookingDto approve(long bookingId, long userId, Boolean approve);
    BookingDto findBookingById(Long bookingId, Long userId);
    Collection<BookingDto> findAllByUserId(long userId, String state);
    Collection<BookingDto> findAllBookingsByOwner(long userId, String state);


}
