package ru.practicum.shareit.booking.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewBookingRequest {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
