package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemRepository {
    Map<Integer, Item> getAll(Long userId);

    Item getById(Long userId, Long itemId);

    List<Item> search(Long userId, String text);

    Item add(long userId, Item item);

    Item update(long itemId, Item item, long userId);

    void deleteItem(long userId, long itemId);
}
