package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static Item mapToItem(Item item) {
        return new Item (
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasItemName()) {
            item.setName(request.getName());
        }
        if (request.hasItemDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.isAvailable() != item.isAvailable()) {
            item.setAvailable(request.isAvailable());
        }
        return item;
    }
}
