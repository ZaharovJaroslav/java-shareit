package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    ItemDTO addNewItem(long itemId, ItemDTO item);

    ItemDTO updateItem(long itemId, long userId, UpdateItemRequest request);

    Item getItemById(long itemId);

    Collection<ItemDTO> getAllItemsUser(long userId);

    Collection<ItemDTO> findItemByNameOrDescription(String text);
    Long findOwnerId(Long itemId);

    ItemDTO findItemById(Long itemId, Long userId);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);


}
