package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(ItemRequestDto requestDto, Long requesterId);
    ItemRequestDto findById(Long userId, Long requestId);
    Collection<ItemRequestDto> findRequests(Long userId);
    Collection<ItemRequestDto> findUserRequests(Long userId);
}
