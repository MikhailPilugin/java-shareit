package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> itemMap;

    @Override
    public Map<Integer, Item> getAll(Long userId) {
        Map<Integer, Item> userItemsMap = new HashMap<>();

        for (Map.Entry<Integer, Item> itemDtoEntry : itemMap.entrySet()) {
            if (itemDtoEntry.getValue().getOwner() == userId) {
                if (userItemsMap.size() > 0) {
                    userItemsMap.put(userItemsMap.size() + 1, itemDtoEntry.getValue());
                } else {
                    userItemsMap.put(1, itemDtoEntry.getValue());
                }
            }
        }

        return userItemsMap;
    }

    @Override
    public Item getById(Long userId, Long itemId) {
        Item item = new Item();

        if (!itemMap.isEmpty()) {
            for (int i = 1; i <= itemMap.size(); i++) {
                if (itemMap.get(i).getId() == itemId) {
                    item = itemMap.get(i);
                    break;
                }
            }
        }
        return item;
    }

    @Override
    public List<Item> search(Long userId, String text) {
        List<Item> itemDtoList = new ArrayList<>();

        if (!text.isEmpty()) {
            Pattern pattern = Pattern.compile(text.toLowerCase(Locale.ROOT));

            for (Map.Entry<Integer, Item> itemDtoEntry : itemMap.entrySet()) {
                String searchName = itemDtoEntry.getValue().getName().toLowerCase();
                String searchDescription = itemDtoEntry.getValue().getDescription().toLowerCase();

                Matcher matcherName = pattern.matcher(searchName);
                Matcher matcherDescription = pattern.matcher(searchDescription);

                boolean matchNameFound = matcherName.find();
                boolean matchDescriptionFound = matcherDescription.find();

                if (matchNameFound || matchDescriptionFound) {
                    if (itemDtoEntry.getValue().getAvailable() == true) {
                        itemDtoList.add(itemDtoEntry.getValue());
                    }
                }
            }
        }

        return itemDtoList;
    }

    @Override
    public Item add(long userId, Item item) {
        boolean userIsFounded = false;

//        for (Map.Entry<Integer, User> userEntry : UserRepositoryImpl.userMap.entrySet()) {
//            if (userEntry.getValue().getId() == userId) {
//                userIsFounded = true;
//            }
//        }

        for (Integer integer : UserRepositoryImpl.setId) {
            if (integer == userId) {
                userIsFounded = true;
            }
        }

        if (!userIsFounded) {
            throw new IllegalArgumentException("User not found!");
        }

        item.setOwner(userId);
        Integer idItem = itemMap.size();

        if (idItem > 0) {
            item.setId(idItem + 1);
            itemMap.put(idItem + 1, item);
        } else {
            item.setId(1);
            itemMap.put(1, item);
        }

        return item;
    }

    @Override
    public Item update(long itemId, Item item, long userId) {
        Item itemDto = new Item();

        Long id;
        if (item.getId() == 0) {
            id = null;
        } else {
            id = item.getId();
        }

        String name = item.getName();
        String description = item.getDescription();
        Boolean available = item.getAvailable();

        Long ownerId;
        if (item.getOwner() == 0) {
            ownerId = null;
        } else {
            ownerId = item.getOwner();
        }

        for (int i = 1; i <= itemMap.size(); i++) {
            if (itemMap.get(i).getId() == itemId) {
                if (itemMap.get(i).getOwner() == userId) {

                    if (name == null) {
                        String newName = itemMap.get(i).getName();
                        itemDto.setName(newName);
                    } else {
                        itemDto.setName(name);
                    }

                    if (description == null) {
                        String newDescription = itemMap.get(i).getDescription();
                        itemDto.setDescription(newDescription);
                    } else {
                        itemDto.setDescription(description);
                    }

                    if (available == null) {
                        boolean newAvailable = itemMap.get(i).getAvailable();
                        itemDto.setAvailable(newAvailable);
                    } else {
                        itemDto.setAvailable(available);
                    }

                    itemDto.setId(itemId);

                    if (ownerId != null) {
                        itemDto.setOwner(ownerId);
                    } else {
                        itemDto.setOwner(userId);
                    }

                    itemMap.put(i, itemDto);
                } else {
                    throw new IllegalArgumentException("Wrong user id");
                }
            }
        }

        return itemDto;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        for (Map.Entry<Integer, Item> itemDtoEntry : itemMap.entrySet()) {
            if (itemDtoEntry.getValue().getOwner() == userId && itemDtoEntry.getValue().getId() == itemId) {
                itemMap.remove(userId, itemDtoEntry);
            }
        }
    }
}
