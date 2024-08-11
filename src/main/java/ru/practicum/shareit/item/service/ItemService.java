package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    ItemDto addNewItem(long itemId, Item item);
    ItemDto updateItem(long itemId, long userId, UpdateItemRequest request);
    ItemDto getItemById(long itemId);
    Collection<ItemDto> getAllItemsUser(long userId);
    Collection<ItemDto> findItemByNameOrDescription(String text);

}
