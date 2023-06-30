package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, ItemDto> itemMap;

    @Override
    public Map<Integer, ItemDto> getAll() {
        return itemMap;
    }

    @Override
    public Map<Integer, ItemDto> getById(long userId) {
        Map<Integer, ItemDto> userItemMap = new HashMap<>();

        if (!itemMap.isEmpty()) {
            for (int i = 1; i <= itemMap.size(); i++) {
                if (itemMap.get(i).getOwner() == userId) {
                    if (!userItemMap.isEmpty()) {
                        int index = itemMap.size() + 1;
                        userItemMap.put(index, itemMap.get(i));
                    } else {
                        userItemMap.put(1, itemMap.get(i));
                    }
                }
            }
        }

        return userItemMap;
    }

    @Override
    public ItemDto add(long userId, ItemDto item) {
        boolean userIsFounded = false;

        for (Map.Entry<Integer, User> userEntry : UserRepositoryImpl.userMap.entrySet()) {
            if (userEntry.getValue().getId() == userId) {
                userIsFounded = true;
            }
        }

        if (!userIsFounded) {
            throw new IllegalArgumentException("User not found!");
        }

        item.setOwner(userId);
        Set<Long> itemIdSet = new HashSet<>();

        if (!itemMap.isEmpty()) {
            for (Map.Entry<Integer, ItemDto> itemDtoEntry : itemMap.entrySet()) {
                itemIdSet.add(itemDtoEntry.getValue().getId());
            }
        }

        if (!itemIdSet.isEmpty()) {
            int itemId = itemIdSet.size() +1;
            item.setId(itemId);
            itemMap.put((int) userId, item);
        } else {
            item.setId(1);
            itemMap.put((int) userId, item);
        }

        return item;
    }

    @Override
    public ItemDto update(long itemId, ItemDto item, long userId) {

//        System.out.println("Users size: " + UserRepositoryImpl.userMap.values());
//        System.out.println("Items size: " + itemMap.values());

        for (int i = 1; i <= itemMap.size(); i++) {
            if (itemMap.get(i).getId() == itemId) {
                if (itemMap.get(i).getOwner() == userId) {
                    long ownerId = itemMap.get(i).getOwner();

                    if (item.getName() == null) {
                        String name = itemMap.get(i).getName();
                        item.setName(name);
                    }

                    if (item.getDescription() == null) {
                        String description = itemMap.get(i).getDescription();
                        item.setDescription(description);
                    }

                    if (item.getAvailable() == null) {
                        boolean available = itemMap.get(i).getAvailable();
                        item.setAvailable(available);
                    }

                    item.setId(itemId);
                    item.setOwner(ownerId);

                    itemMap.replace(i, item);
                } else {
                    throw new IllegalArgumentException("Wrong user id");
                }
                break;
            }
        }

        return item;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        for (Map.Entry<Integer, ItemDto> itemDtoEntry : itemMap.entrySet()) {
            if (itemDtoEntry.getValue().getOwner() == userId && itemDtoEntry.getValue().getId() == itemId) {
                itemMap.remove(userId, itemDtoEntry);
            }
        }
    }
}
