package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getAll();

    Collection<ItemDto> getById(long userId);

    ItemDto add(Long userId, ItemDto item);

    ItemDto update(long itemId, ItemDto item, long userId);

    void deleteItem(long userId, long itemId);
}
