package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
public class BookingDto {
    Long id;
    String item;
    BookingStatus status;

    private static BookingDto toBookingDto(Booking booking) {
        return new BookingDto (booking.getId(),
                   booking.getItem().getName(),
                   booking.getStatus()
        );
    }


}
