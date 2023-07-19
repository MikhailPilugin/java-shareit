package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.BookingNotAvailableException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto add(long userId, ItemDto dto) {
        Item item = itemMapper.toItem(dto);
        User user = userService.getById(userId);
        item = item.withOwner(user);
        item = itemRepository.save(item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto dto) {
        User user = userService.getById(userId);
        Item item = itemMapper.toItem(dto);
        item = item.withId(itemId).withOwner(user);
        checkItemOwner(item);
        item = updateFields(item);
        item = itemRepository.save(item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Item not found"));
        List<CommentDto> commentDto = commentMapper.toCommentDto(commentRepository.findByItemId(itemId));
        List<Booking> bookings;
        if (item.getOwner().getId() != userId) {
            return itemMapper.toOwnerDto(item, commentDto,null, null);
        } else {
            bookings = bookingRepository.findAllByItemId(itemId);
            return itemMapper.toOwnerDto(item, commentDto,
                    defineLastBooking(bookings), defineNextBooking(bookings));
        }
    }

    @Override
    public void deleteItem(long itemId) {
        checkItemExistence(itemId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemOwnerDto> getAll(long userId) {
        userService.checkUser(userId);
        Map<Long, Item> mapItems = itemRepository.findAllByOwnerId(userId)
                .stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));
        Map<Long, List<Booking>> mapBookings = bookingRepository.findAllByItemIdAndOwnerId(mapItems.keySet())
                .stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
        Map<Long, List<Comment>> mapComments = commentRepository.findAllByItemId(mapItems.keySet())
                .stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
        return mapItems.values()
                .stream()
                .map(item -> itemMapper.toOwnerDto(item,
                        commentMapper.toCommentDto(mapComments.getOrDefault(item.getId(), new ArrayList<>())),
                        defineLastBooking(mapBookings.get(item.getId())),
                        defineNextBooking(mapBookings.get(item.getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.findAllByQuery(query.toLowerCase());
        return itemMapper.toItemDto(items);
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User author = userService.getById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Item not found"));
        if (bookingRepository.findSuccessfulUserBooking(itemId, userId).isEmpty()) {
            throw new BookingNotAvailableException("Item is not available");
        }
        Comment comment = commentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    private void checkItemExistence(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Item not found");
        }
    }

    private Item updateFields(Item item) {
        Item saved = itemRepository.findById(item.getId()).orElseThrow(() ->
                new ItemNotFoundException("Item not found"));
        String name;
        if (item.getName() == null || item.getName().isBlank()) {
            name = saved.getName();
        } else {
            name = item.getName();
        }
        String description;
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            description = saved.getDescription();
        } else {
            description = item.getDescription();
        }
        boolean available = item.getAvailable() == null ? saved.getAvailable() : item.getAvailable();
        return Item.builder()
                .id(item.getId())
                .name(name)
                .description(description)
                .available(available)
                .owner(saved.getOwner())
                .build();
    }

    private void checkItemOwner(Item item) {
        Optional<Long> ownerId = itemRepository.findOwnerIdByItemId(item.getId());
        if (ownerId.isEmpty() || !ownerId.get().equals(item.getOwner().getId())) {
            throw new ItemNotFoundException("User not found");
        }
    }

    private Booking defineLastBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        } else {
            LocalDateTime now = LocalDateTime.now();
            return bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(now))
                    .max(Comparator.comparing(Booking::getStart))
                    .orElse(null);
        }
    }

    private Booking defineNextBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        } else {
            LocalDateTime now = LocalDateTime.now();
            return bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .filter(booking -> !booking.getStatus().equals(Status.REJECTED))
                    .min(Comparator.comparing(Booking::getStart))
                    .orElse(null);
        }
    }
}
