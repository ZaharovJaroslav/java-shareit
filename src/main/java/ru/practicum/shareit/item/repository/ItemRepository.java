package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item saveItem(Item item);
    Item updateItem(long itemId, Item item);
    Item getItemById(long itemId);
    Collection<Item> getAllItemsUser(long UserId);
    Item findItemByNameOrDescription(String text);
}
