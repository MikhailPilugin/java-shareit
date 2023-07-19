package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto add(long userId, BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null
                || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDateTimeException("Wrong dates of booking");
        }
        Booking booking = bookingMapper.mapToBooking(bookingDto);
        User user = userService.getById(userId);
        booking.setBooker(user);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new ItemNotFoundException("Item not found"));
        if (!item.getAvailable()
                || bookingRepository.hasBooking(item.getId(), booking.getStart(), booking.getEnd())
                .isPresent()) {
            throw new BookingNotAvailableException("Item is not available for booking");
        }
        if (item.getOwner().getId().equals(booking.getBooker().getId())) {
            throw new BookingNotFoundException("Booking not found");
        }
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto update(long userId, long bookingId, String approved) {
        User user = userService.getById(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Booking not found"));
        if (!booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new UserNotFoundException("User not found");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new IllegalArgumentException("Wrong status");
        }
        try {
            boolean isApprove = Boolean.parseBoolean(approved);
            if (isApprove) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            booking = bookingRepository.save(booking);
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong state");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Booking not found"));
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<BookingDto> getAll(long userId, String stateQuery) {
        userService.checkUser(userId);
        State state;
        List<Booking> result = new ArrayList<>();
        try {
            state = State.valueOf(stateQuery.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + stateQuery);
        }
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case PAST:
                result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case CURRENT:
                result = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
        }
        return bookingMapper.toBookingDto(result);
    }

    @Override
    public List<BookingDto> getByOwner(long userId, String stateQuery) {
        userService.checkUser(userId);
        State state;
        List<Booking> result = new ArrayList<>();
        try {
            state = State.valueOf(stateQuery.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + stateQuery);
        }

        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                break;
            case PAST:
                result = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case FUTURE:
                result = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case CURRENT:
                result = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
                break;
            case WAITING:
                result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
        }
        return bookingMapper.toBookingDto(result);
    }
}
