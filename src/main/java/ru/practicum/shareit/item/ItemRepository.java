package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

public interface ItemRepository {
    Map<Integer, ItemDto> getAll(Long userId);

    ItemDto getById(Long userId, Long itemId);

    ItemDto add(long userId, ItemDto item);

    ItemDto update(long itemId, ItemDto item, long userId);

    void deleteItem(long userId, long itemId);
}
