package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
public final class ItemRequestDto {
    private String description;
    private User requestor;
    private LocalDateTime created;

    private static ItemRequestDto itemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getDescription(),
                                  itemRequest.getRequestor(),
                                  itemRequest.getCreated()
        );
    }
}
