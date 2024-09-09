package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;


    @Override
    public ItemDTO addNewItem(long userId, ItemDTO item) {
        log.debug("addNewItem({}, {})", userId,item);
        itemValidation(item);
        User user = userService.getUserById(userId);

        Item newItem = ItemMapper.mapToItem(item);
        newItem.setOwnerId(user.getId());
        repository.save(newItem);
        return ItemMapper.toItemDTO(newItem);
      //  return repository.findById(repository.saveItem(newItem));
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
        Item updatedItem = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("инструмент не найден"));
        if (updatedItem.getOwnerId() != userId) {
            throw new NotFoundException("У пользователя нет такого инcтрумента");
        }
        ItemMapper.updateItemFields(updatedItem, request);

        return ItemMapper.toItemDTO(repository.findById(itemId).get());
    }

    @Override
    public Item getItemById(long itemId) {
        log.debug("getItemById({})", itemId);
       // ItemDTO result;

        Item item = repository.findById(itemId)
                .orElseThrow(()-> new NotFoundException("Инструмента с таким id не существует"));



      //  result = ItemMapper.toItemDTO(item);
        //return item;
return item;

      //  return result;
    }

    @Override
    public  Collection<ItemDTO> getAllItemsUser(long userId) {
        log.debug("getAllItemsUser({})", userId);
        userService.getUserById(userId);

        return repository.findAllByOwnerId(userId).stream()
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
    @Override
    public Long findOwnerId(Long itemId) {
        return repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with ID = %d not found.", itemId)))
                .getOwnerId();
    }
}

