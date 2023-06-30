package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Collection<Item> getAll(Long userId);

    Item getById(Long userId, Long itemId);

    List<Item> search(Long userId, String text);

    Item add(Long userId, Item item);

    Item update(long itemId, Item item, long userId);

    void deleteItem(long userId, long itemId);
}
