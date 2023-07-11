package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Collection<ItemDto> getAll(Long userId) {
        Collection<Item> itemCollection = itemRepository.findAll();
        Collection<ItemDto> itemDtoCollection = new ArrayList<>();

        for (Item item : itemCollection) {
            itemDtoCollection.add(ItemMapper.toItemDto(item));
        }

        return itemDtoCollection;
    }

//    @Override
//    public ItemDto getById(Long userId, Long itemId) {
////        return ItemMapper.toItemDto(itemRepository.getById(userId, itemId));
//        return ItemMapper.toItemDto(itemRepository.findById(userId));
//    }

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        Item item = new Item();

        if (itemDto.getAvailable() == null) {
            throw new RuntimeException("There is no available parameter in the request");
        }

        if (itemDto.getName().isEmpty() || itemDto.getName().isBlank()) {
            throw new RuntimeException("There is no name in the request");
        }

        if (itemDto.getDescription() == null) {
            throw new RuntimeException("There is no description in the request");
        }

//        item = itemRepository.add(userId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
//        List<Item> itemList = itemRepository.search(userId, text);
        List<Item> itemList = new ArrayList<>();
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Item item : itemList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }

        return itemDtoList;
    }

    @Override
    public ItemDto update(long itemId, ItemDto itemDto, long userId) {
//        Item item = itemRepository.update(itemId, ItemMapper.toItem(itemDto), userId);
        Item item = new Item();

        return ItemMapper.toItemDto(item);
    }

//    @Override
//    public void deleteItem(long userId, long itemId) {
////        itemRepository.deleteItem(userId, itemId);
//        itemRepository.delete(userId);
//    }
}
