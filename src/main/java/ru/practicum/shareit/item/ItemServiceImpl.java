package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exceptions.BookingNotAvailableException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private ItemMapper itemMapper;
    private CommentMapper commentMapper;

    @Autowired
    public void setItemMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Autowired
    public void setCommentMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public ItemDto add(long userId, ItemDto dto) {
        Item item = itemMapper.toItem(dto);
        User owner = userService.getById(userId);
        item.setOwner(owner);
        if (dto.getRequestId() != null) {
            long itemRequestId = dto.getRequestId();
            ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                    .orElseThrow(() -> new ItemRequestNotFoundException("Item request with id=" + itemRequestId + " not found"));
            item.setItemRequest(itemRequest);
        }
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
    public ItemOwnerDto getById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with id=%d not found", itemId)));
        List<CommentDto> commentDtoList = commentMapper.toCommentDto(commentRepository.findByItemId(itemId));
        List<Booking> bookings;
        if (item.getOwner().getId() != userId) {
            return itemMapper.toOwnerDto(item, commentDtoList, null, null);
        } else {
            bookings = bookingRepository.findAllByItemId(itemId);
            return itemMapper.toOwnerDto(item, commentDtoList,
                    defineLastBooking(bookings), defineNextBooking(bookings));
        }

    }

    @Override
    public void deleteById(long itemId) {
        checkItemExistence(itemId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemOwnerDto> getAll(long userId, int from, int size) {
        userService.checkUser(userId);
        int offset = from > 0 ? from / size : 0;
        Pageable page = PageRequest.of(offset, size);
        Map<Long, Item> mapItems = itemRepository.findAllByOwnerId(userId, page)
                .stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));
        Map<Long, List<Booking>> mapBookings = bookingRepository.findAllBookings(mapItems.keySet())
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
    public List<ItemDto> search(String query, int from, int size) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        int offset = from > 0 ? from / size : 0;
        Pageable page = PageRequest.of(offset, size);
        return itemMapper.toItemDto(itemRepository.findAllByQuery(query.toLowerCase(), page));
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User author = userService.getById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with id=%d not found", itemId)));
        if (bookingRepository.findSuccessfulUserBooking(itemId, userId).isEmpty()) {
            throw new BookingNotAvailableException(
                    String.format("User with id=%d don't use item with id=%d", userId, itemId));
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
            throw new ItemNotFoundException(String.format("Item with id=%d not found", itemId));
        }
    }

    private Item updateFields(Item item) {
        Item saved = itemRepository.findById(item.getId()).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with id=%d not found", item.getId())));
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
            throw new ItemNotFoundException(
                    String.format("User with id=%d is not owner of the item with id=%d",
                            item.getOwner().getId(), item.getId()));
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