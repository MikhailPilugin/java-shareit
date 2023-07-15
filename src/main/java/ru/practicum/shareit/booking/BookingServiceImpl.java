package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Override
    public Booking add(Long userId, BookingDto bookingDto) {
        Booking booking;
        User user;
        Item item;
        BookingDto bookingDtoNew;

        if (userService.getUser(userId) != null) {
            user = userService.getUser(userId);
        } else {
            throw new RuntimeException("User not found");
        }

        if (itemService.getById(userId, bookingDto.getItemId()) == null) {
            throw new RuntimeException("Item id not found");
        }

        bookingDto.setUserId(userId);
        bookingDto.setStatus(String.valueOf(Status.WAITING));

        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();

        if (start.isAfter(end)) {
            throw new RuntimeException("End time in past");
        } else if (start.isEqual(end)) {
            throw new RuntimeException("Start time is equals end time");
        } else if (start == null || end == null) {
            throw new RuntimeException("Start time or end time is null");
        }

        ItemDto itemDto = itemService.getById(userId, bookingDto.getItemId());

        if (!itemDto.getAvailable()) {
            throw new RuntimeException("Item is not available");
        }

        item = ItemMapper.toItem(itemDto);
        booking = BookingMapper.toBooking(bookingDto);

        booking.setBooker(user);
        booking.setItem(item);

        booking.setStatus(Status.valueOf(bookingDto.getStatus()));

        bookingRepository.save(booking);

        return booking;
    }
}
