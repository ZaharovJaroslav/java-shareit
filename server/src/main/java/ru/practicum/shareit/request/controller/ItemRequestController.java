package ru.practicum.shareit.request.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto addNewItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.debug("Добавление новго запроса на инструмент");
        return requestService.addNewItemRequest(itemRequestDto, userId);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto findRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long requestId) {
        log.debug("Получить запрос по id");
        return requestService.findById(userId, requestId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получить список запросов, созданных другими пользователями");
        return requestService.findRequests(userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получить список своих запросов вместе с данными об ответах на них");
        return requestService.findUserRequests(userId);
    }


}
