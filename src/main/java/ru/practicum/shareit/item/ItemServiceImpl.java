package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepositoryImpl itemRepository;

    @Override
    public Collection<ItemDto> getAll() {
        return itemRepository.getAll().values();
    }

    @Override
    public Collection<ItemDto> getById(long userId) {
        return itemRepository.getById(userId).values();
    }

    @Override
    public ItemDto add(Long userId, ItemDto item){
        if (item.isAvailable() == false) {
            throw new RuntimeException("Item is not available");
        }

        if (item.getName().isEmpty() || item.getName().isBlank()) {
            throw new RuntimeException("There is no name in the request");
        }

        if (item.getDescription() == null) {
            throw new RuntimeException("There is no description in the request");
        }

        return itemRepository.add(userId, item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteItem(userId, itemId);
    }
}
