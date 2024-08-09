package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    private static ItemRequest mapToItemRequest(ItemRequest itemRequest) {
        return new ItemRequest(itemRequest.getId(),
                               itemRequest.getDescription(),
                               itemRequest.getRequestor(),
                               LocalDateTime.now()
        );
    }
}
