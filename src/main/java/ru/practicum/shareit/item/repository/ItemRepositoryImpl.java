package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
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
        log.debug("Item updateItem({})", item);
        return getItemById(item.getId());
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
        log.debug("findItemByNameOrDescription({})", text);
        Collection<Item> foundItems = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return foundItems;
        } else {
            foundItems = items.values().stream()
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text))
                    .filter(Item::getAvailable)
                    .collect(Collectors.toList());
        }
        return foundItems;
    }
}
