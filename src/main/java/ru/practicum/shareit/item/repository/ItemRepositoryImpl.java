package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


import java.util.HashMap;
import java.util.Map;

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
        generationNextId();
        item.setId(id);
        items.put(id,item);

        return getItemById(item.getId());
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        return null;
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
    public Item getAllItemsUser(long UserId) {
        return null;
    }

    @Override
    public Item findItemByNameOrDescription(String text) {
        return null;
    }
}
