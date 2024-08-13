package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Data
@AllArgsConstructor
public class ItemDto {
    Long id;
    String name;
    String description;
    boolean isAvailable;
    Long requestId;

    public static ItemDto toItemDto(Item item) {
        log.debug("toItemDto({})",item);
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
