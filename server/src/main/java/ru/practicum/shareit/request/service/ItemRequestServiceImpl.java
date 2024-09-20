package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;

import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    UserService userService;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;



    @Override
    public ItemRequestDto addNewItemRequest(ItemRequestDto request, Long requestorId) {
        ItemRequest itemRequest = ItemRequest.builder()
                .description(request.getDescription())
                .creator((userService.getUserById(requestorId)))
                .created(LocalDateTime.now())
                .build();
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public ItemRequestDto findById(Long userId, Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос на аренду с id = " + requestId + " не найден"));
        itemRequest.setItems(itemRepository.findAllByItemRequest(itemRequest));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setRequestor(UserMapper.toUserDto(userService.getUserById(userId)));
        return itemRequestDto;
    }

    @Override
    public Collection<ItemRequestDto> findRequests(Long userId) {
            userService.getUserById(userId);
            Collection<ItemRequestDto> list = new ArrayList<>();
        Collection<ItemRequest> findAllByRequesterIdIsNot = itemRequestRepository.findByCreatorIdIsNot(userId);
            findAllByRequesterIdIsNot.forEach(itemRequest -> {
                itemRequest.setItems(itemRepository.findAllByItemRequest(itemRequest));
                ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
                list.add(itemRequestDto);
            });
            return list;
    }

    @Override
    public Collection<ItemRequestDto> findUserRequests(Long userId) {
        userService.getUserById(userId);
        Collection<ItemRequestDto> list = new ArrayList<>();
        Collection<ItemRequest> findAllByCreatorIdOrderByCreatedDesc = itemRequestRepository.findByCreatorIdOrderByCreatedDesc(userId);
        findAllByCreatorIdOrderByCreatedDesc.forEach(i -> {
            i.setItems(itemRepository.findAllByItemRequest(i));
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(i);
            list.add(itemRequestDto);
        });
        return list;
    }
}
