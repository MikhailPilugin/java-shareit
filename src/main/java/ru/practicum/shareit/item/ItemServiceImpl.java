package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemDto> getAll(Long userId) {
        Collection<Item> itemCollection = itemRepository.findAll();
        Collection<ItemDto> itemDtoCollection = new ArrayList<>();

        for (Item item : itemCollection) {
            if (item.getOwner().getId() == userId) {
                itemDtoCollection.add(ItemMapper.toItemDto(item));
            }
        }

        return itemDtoCollection;
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
//        return ItemMapper.toItemDto(itemRepository.getById(userId, itemId));
//        return ItemMapper.toItemDto(itemRepository.findById(userId));

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = new Item();

        if (optionalItem.isPresent()) {
            item = optionalItem.get();
        } else {
            throw new IllegalArgumentException("Wrong item id");
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        Item item = new Item();
        Optional<User> optionalItem = userRepository.findById(userId);

        if (itemDto.getAvailable() == null) {
            throw new RuntimeException("There is no available parameter in the request");
        }

        if (itemDto.getName().isEmpty() || itemDto.getName().isBlank()) {
            throw new RuntimeException("There is no name in the request");
        }

        if (itemDto.getDescription() == null) {
            throw new RuntimeException("There is no description in the request");
        }

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        if (optionalItem.isPresent()) {
            item.setOwner(optionalItem.get());
        } else {
            throw new IllegalArgumentException("User not found");
        }


        itemRepository.save(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
//        List<Item> itemList = itemRepository.search(userId, text);
        List<Item> itemList = itemRepository.search(text);
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Item item : itemList) {
            if (item.getAvailable() && !text.isEmpty()) {
                itemDtoList.add(ItemMapper.toItemDto(item));
            }
        }

        return itemDtoList;
    }

    @Override
    public ItemDto update(long itemId, ItemDto itemDto, long userId) {
//        Item item = itemRepository.update(itemId, ItemMapper.toItem(itemDto), userId);
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.get();
        User user = item.getOwner();

        if (item.getOwner().getId() == userId) {

            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }

            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }

            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }

            if (itemDto.getOwner() != null) {
                item.setOwner(user);
            }

        } else {
            throw new IllegalArgumentException("Wrong user id");
        }

        itemRepository.save(item);

        return ItemMapper.toItemDto(item);
    }

//    @Override
//    public void deleteItem(long userId, long itemId) {
////        itemRepository.deleteItem(userId, itemId);
//        itemRepository.delete(userId);
//    }
}
