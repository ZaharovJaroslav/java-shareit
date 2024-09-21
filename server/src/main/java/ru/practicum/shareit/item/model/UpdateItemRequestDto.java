package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateItemRequestDto {
    private String name;
    private String description;
    private Boolean available;

    public boolean hasItemName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasItemDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasItemAvailable() {
        return ! (available == null);
    }



}
