package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.util.List;

public interface ItemService {

    ItemDto add(long userId, ItemDto dto);

    ItemDto update(long userId, long itemId, ItemDto dto);

    ItemDto getById(long userId, long itemId);

    void deleteItem(long itemId);

    List<ItemOwnerDto> getAll(long userId);

    List<ItemDto> search(String query);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
