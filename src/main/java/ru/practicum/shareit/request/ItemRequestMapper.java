package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
