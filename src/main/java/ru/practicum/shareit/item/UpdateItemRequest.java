package ru.practicum.shareit.item;

import lombok.Data;

@Data
public class UpdateItemRequest {
    private String name;
    private String description;
    private boolean available;

    public boolean hasItemName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasItemDescription() {
        return ! (description == null || description.isBlank());
    }



}
