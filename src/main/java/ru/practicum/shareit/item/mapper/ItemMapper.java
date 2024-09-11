package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {



    public static Item mapToItem(ItemDto item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

    }


    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item updateItemFields(Item item, UpdateItemRequestDto request) {
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
