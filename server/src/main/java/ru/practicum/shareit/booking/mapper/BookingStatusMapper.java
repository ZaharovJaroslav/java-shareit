package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;

@Component
public class BookingStatusMapper {

    public static BookingStatus toBookingStatus(String state) {
        switch (state) {
            case "ALL":
                return BookingStatus.ALL;
            case "CURRENT":
                return BookingStatus.CURRENT;
            case "PAST":
                return BookingStatus.PAST;
            case "FUTURE":
                return BookingStatus.FUTURE;
            case "WAITING":
                return BookingStatus.WAITING;
            case "REJECTED":
                return BookingStatus.REJECTED;
        }
        throw new BadRequestException("Введен некорректный запрос");
    }
}
