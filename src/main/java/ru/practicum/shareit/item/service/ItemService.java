package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.Collection;

public interface ItemService {
    ItemDTO addNewItem(long itemId, ItemDTO item);

    ItemDTO updateItem(long itemId, long userId, UpdateItemRequest request);

    ItemDTO getItemById(long itemId);

    Collection<ItemDTO> getAllItemsUser(long userId);

    Collection<ItemDTO> findItemByNameOrDescription(String text);

}
