package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

@Data
@RequiredArgsConstructor
public class ItemDto {
    private long id;
    private final String name;
    private final String description;
    private final boolean available;
    private String owner;
    private ItemRequest request;
}
