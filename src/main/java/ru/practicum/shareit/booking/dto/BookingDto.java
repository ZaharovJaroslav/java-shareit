package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
public class BookingDto {
    Long id;
    String item;
    BookingStatus status;

    private static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                   booking.getItem().getName(),
                   booking.getStatus()
        );
    }


}
