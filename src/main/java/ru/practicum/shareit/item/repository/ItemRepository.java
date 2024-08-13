package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item saveItem(Item item);

    Item updateItem(Item item);

    Item getItemById(long itemId);

    Collection<Item> getAllItemsUser(long userId);

    Collection<Item> findItemByNameOrDescription(String text);
}
