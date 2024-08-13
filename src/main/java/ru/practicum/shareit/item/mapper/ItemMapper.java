package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static Item mapToItem(Item item) {
        log.debug("mapToItem({})",item);
        return new Item (
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        log.debug("pdateItemFields({}, {})",item,request);
        if (request.hasItemName()) {
            item.setName(request.getName());
        }
        if (request.hasItemDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasItemAvailable()) {
            item.setAvailable(request.getAvailable());
        }
        return item;
    }
}
