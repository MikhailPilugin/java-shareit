package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Collection<ItemDto> getAll(Long userId);

    ItemDto getById(Long userId, Long itemId);

    List<ItemDto> search(Long userId, String text);

    ItemDto add(Long userId, ItemDto item);

    ItemDto update(long itemId, ItemDto item, long userId);

//    void deleteItem(long userId, long itemId);
}
