package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private  final UserService userService;


    @Override
    public ItemDto addNewItem(long itemId, Item item) {
        log.debug("addNewItem({}, {})", itemId,item);
        itemValidation(item);
        item.setOwner(userService.getUserById(itemId));

        return ItemDto.toItemDto(repository.saveItem(item)) ;

    }

    public void itemValidation(Item item) {
        log.info("itemValidation({})", item);
        if (item.getName() == null || item.getName().isBlank()) {
            throw new NotFoundException("Название инстумента не задано");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new NotFoundException("Описание инстумента не задано");
        }
//        if (!item.isAvailable()) {
//            throw new NotFoundException("При добавлении нового инстумента он должен быть доступен");
//        }
    }

    @Override
    public ItemDto updateItem(long itemId, Item item) {
        return null;
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return ItemDto.toItemDto(repository.getItemById(itemId));
    }

    @Override
    public Collection<ItemDto> getAllItemsUser(long userId) {
        log.debug("getAllItemsUser({})", userId);
        User item = userService.getUserById(userId);
        /*return repository.getAllItemsUser(userId).stream()
                   .map(ItemDto::toItemDto)
                   .collect(Collectors.toList());*/
        return repository.getAllItemsUser(userId).stream()
                .map(ItemDto::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findItemByNameOrDescription(String text) {
        return null;
    }
}

