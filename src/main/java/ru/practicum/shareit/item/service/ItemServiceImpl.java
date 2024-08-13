package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;


    @Override
    public ItemDto addNewItem(long itemId, Item item) {
        log.debug("addNewItem({}, {})", itemId,item);
        itemValidation(item);
        User user = userService.getUserById(itemId);
        item.setOwner(user);

        return ItemDto.toItemDto(repository.saveItem(item)) ;

    }

    public void itemValidation(Item item) {
        log.info("itemValidation({})", item);
        if ( item.getAvailable() == null) {
            throw new ValidationException("При добавлении нового инстумента он должен быть доступен");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Название инстумента не задано");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Описание инстумента не задано");
        }
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, UpdateItemRequest request) {
        log.debug("updateItem({}, {}, {})",itemId, userId, request);
        Item updatedItem = repository.getItemById(itemId);
        if (updatedItem.getOwner().getId() != userId) {
            throw new NotFoundException("У пользователя нет такоего иснтрумента");
        }
        updatedItem = repository.updateItem(ItemMapper.updateItemFields(updatedItem, request));

        return ItemDto.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        log.debug("getItemById({})", itemId);
        return ItemDto.toItemDto(repository.getItemById(itemId));
    }

    @Override
    public Collection<ItemDto> getAllItemsUser(long userId) {
        log.debug("getAllItemsUser({})", userId);
        userService.getUserById(userId);

        return repository.getAllItemsUser(userId).stream()
                .map(ItemDto::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> findItemByNameOrDescription(String text) {
        log.debug("findItemByNameOrDescription({})", text);

        return repository.findItemByNameOrDescription(text).stream()
                .map(ItemDto::toItemDto)
                .collect(Collectors.toList());
    }
}

