package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
   long saveItem(Item item);

    void updateItem(Item item);

    Optional<Item> getItemById(long itemId);

    Collection<Item> getAllItemsUser(long userId);

    Collection<Item> findItemByNameOrDescription(String text);
}
