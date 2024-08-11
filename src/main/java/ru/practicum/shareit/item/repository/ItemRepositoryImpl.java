package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static long id;
    private final Map<Long,Item> items;

    public ItemRepositoryImpl() {
        this.items = new HashMap<>();
    }

    private void generationNextId() {
        log.debug("Генерация id для нового интсрумента");
        ++id;
        log.debug("id = {} ", id);
    }


    @Override
    public Item saveItem(Item item) {
        log.debug("saveItem({})", item);
        generationNextId();
        item.setId(id);
        items.put(id,item);

        return getItemById(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        return  getItemById(item.getId());
    }

    @Override
    public Item getItemById(long itemId) {
        log.debug("getItemById({})", itemId);
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Инструмента с таким id не существует");
        }
        return items.get(itemId);

    }

    @Override
    public Collection<Item> getAllItemsUser(long userId) {
        log.debug("getAllItemsUser({})", userId);
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> findItemByNameOrDescription(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }
}
