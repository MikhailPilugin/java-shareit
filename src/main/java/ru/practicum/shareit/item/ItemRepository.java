package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

public interface ItemRepository {
    Map<Integer, ItemDto> getAll();

    Map<Integer, ItemDto> getById(long userId);

    ItemDto add(long userId, ItemDto item);

    void deleteItem(long userId, long itemId);
}
