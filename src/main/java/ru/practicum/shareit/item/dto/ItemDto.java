package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@RequiredArgsConstructor
public class ItemDto {
    private long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Boolean available;
    private long owner;

    private ItemRequest request;
}
