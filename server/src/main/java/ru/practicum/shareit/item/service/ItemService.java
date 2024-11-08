package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.UpdateItemRequestDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addNewItem(long itemId, ItemDto item);

    ItemDto updateItem(long itemId, long userId, UpdateItemRequestDto request);

    Item getItemById(long itemId);

    Collection<ItemDto> getAllItemsUser(long userId);

    Collection<ItemDto> findItemByNameOrDescription(String text);

    Long findOwnerId(Long itemId);

    ItemDto findItemById(Long itemId, Long userId);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);


}
