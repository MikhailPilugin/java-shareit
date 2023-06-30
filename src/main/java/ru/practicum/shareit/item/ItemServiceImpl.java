package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryImpl itemRepository;

    @Override
    public Collection<Item> getAll(Long userId) {
        return itemRepository.getAll(userId).values();
    }

    @Override
    public Item getById(Long userId, Long itemId) {
        return itemRepository.getById(userId, itemId);
    }

    @Override
    public Item add(Long userId, Item item) {

        if (item.getAvailable() == null) {
            throw new RuntimeException("There is no available parameter in the request");
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
    public List<Item> search(Long userId, String text) {
        return itemRepository.search(userId, text);
    }

    @Override
    public Item update(long itemId, Item item, long userId) {
        return itemRepository.update(itemId, item, userId);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteItem(userId, itemId);
    }
}
