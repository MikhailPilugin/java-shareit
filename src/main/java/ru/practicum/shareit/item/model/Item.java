package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@RequiredArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long owner;
    private ItemRequest request;
}

