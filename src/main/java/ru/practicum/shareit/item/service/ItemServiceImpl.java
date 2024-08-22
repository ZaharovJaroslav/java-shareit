package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDTO;
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
    public ItemDTO addNewItem(long itemId, ItemDTO item) {
        log.debug("addNewItem({}, {})", itemId,item);
        itemValidation(item);
        User user = userService.getUserById(itemId);

        Item newItem = ItemMapper.mapToItem(item);
        newItem.setOwner(user);

        return ItemMapper.toItemDTO(repository.saveItem(newItem));
    }

    public void itemValidation(ItemDTO item) {
        log.info("itemValidation({})", item);

        if (item.getAvailable() == null) {
            throw new ValidationException("При добавлении нового инструмента он должен быть доступен");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Название инструмента не задано");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Описание инструмента не задано");
        }
    }

    @Override
    public ItemDTO updateItem(long itemId, long userId, UpdateItemRequest request) {
        log.debug("updateItem({}, {}, {})",itemId, userId, request);
        Item updatedItem = repository.getItemById(itemId);
        if (updatedItem.getOwner().getId() != userId) {
            throw new NotFoundException("У пользователя нет такого иснтрумента");
        }
        updatedItem = repository.updateItem(ItemMapper.updateItemFields(updatedItem, request));

        return ItemMapper.toItemDTO(updatedItem);
    }

    @Override
    public ItemDTO getItemById(long itemId) {
        log.debug("getItemById({})", itemId);
        return ItemMapper.toItemDTO(repository.getItemById(itemId));
    }

    @Override
    public Collection<ItemDTO> getAllItemsUser(long userId) {
        log.debug("getAllItemsUser({})", userId);
        userService.getUserById(userId);

        return repository.getAllItemsUser(userId).stream()
                .map(ItemMapper::toItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDTO> findItemByNameOrDescription(String text) {
        log.debug("findItemByNameOrDescription({})", text);

        return repository.findItemByNameOrDescription(text).stream()
                .map(ItemMapper::toItemDTO)
                .collect(Collectors.toList());
    }
}

