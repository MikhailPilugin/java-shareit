package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
//    void delete(long userId);
//    Map<Integer, Item> getAll(Long userId);
//
//    Item getById(Long userId, Long itemId);
//
//    List<Item> search(Long userId, String text);
//
//    Item add(long userId, Item item);
//
//    Item update(long itemId, Item item, long userId);
//
//    void deleteItem(long userId, long itemId);
}
