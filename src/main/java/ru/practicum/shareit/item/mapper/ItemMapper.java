package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {


    public static Item mapToItem(ItemDTO item) {
        log.debug("mapToItem({})",item);
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }


    public static ItemDTO toItemDto(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
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
