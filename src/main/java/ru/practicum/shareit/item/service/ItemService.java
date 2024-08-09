package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {
    ItemDto addNewItem(long itemId, Item item);
    ItemDto updateItem(long itemId, Item item);
    ItemDto getItemById(long itemId);
    ItemDto getAllItemsUser(long UserId);
    ItemDto findItemByNameOrDescription(String text);

}
