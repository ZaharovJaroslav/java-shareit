package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    User owner;
    ItemRequest request;
}

