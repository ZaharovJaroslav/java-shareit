package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.request.UpdateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody Item item) {
        log.debug("Добавление нового инструмента для пользвоателя с id - {}, {}", userId,item);
        if (item == null) {
            throw new NotFoundException("Не указан инструмент для добавления");
        }
        return itemService.addNewItem(userId,item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.debug("Получение инструмента по id - {}",itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получить все инструменты пользователя с id - {} для сдачи в аренду", userId);
        return itemService.getAllItemsUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemByNameOrDescription(@RequestParam String text) {
        log.debug("Поиск достпуных для аренды документов по названиию и описанию - {}", text);
        return itemService.findItemByNameOrDescription(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody UpdateItemRequest request,
                              @PathVariable Long itemId) {
        log.debug("Обновление инструмента пользователя - {}, {}", request, itemId);
        return itemService.updateItem(itemId,userId,request);
    }
}