package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Data
@AllArgsConstructor
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;


}
