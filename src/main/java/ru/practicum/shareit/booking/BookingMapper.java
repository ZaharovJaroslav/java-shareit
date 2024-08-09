package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {
    private static Booking mapToBooking(Booking booking) {
        return new Booking(booking.getId(),
                           booking.getStart(),
                           booking.getEnd(),
                           booking.getItem(),
                           booking.getBooker(),
                           booking.getStatus()
        );
    }
}
