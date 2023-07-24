package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private BookingMapper bookingMapper;

    @Autowired
    public void setBookingMapper(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDto add(long userId, BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null
                || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDateTimeException("Wrong dates of booking");
        }
        Booking booking = bookingMapper.toBooking(bookingDto);
        User user = userService.getById(userId);
        booking.setBooker(user);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with id=%d not found", bookingDto.getItemId())));
        if (!item.getAvailable()
                || bookingRepository.findBookingWithSameDate(item.getId(), booking.getStart(), booking.getEnd())
                .isPresent()) {
            throw new BookingNotAvailableException("Item is not available for booking");
        }
        if (item.getOwner().getId().equals(booking.getBooker().getId())) {
            throw new BookingNotFoundException("Owner cannot book his thing");
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
            throw new UserNotFoundException("Item don't belong to the user");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new IllegalArgumentException("Status was confirmed by the owner earlier");
        }
        boolean isApprove = Boolean.parseBoolean(approved);
        if (isApprove) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Booking not found"));
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new UserAccessException("User is prohibited to view the booking information");
        }
    }

    @Override
    public List<BookingDto> getUserBookings(long userId, String stateQuery, int from, int size) {
        userService.checkUser(userId);
        State state;
        Page<Booking> result = Page.empty();
        try {
            state = State.valueOf(stateQuery.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + stateQuery);
        }
        LocalDateTime now = LocalDateTime.now();
        int offset = from > 0 ? from / size : 0;
        Pageable page = PageRequest.of(offset, size);
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
                break;
            case PAST:
                result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now, page);
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now, page);
                break;
            case CURRENT:
                result = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        now, now, page);
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                break;
        }
        return bookingMapper.toBookingDto(result);
    }

    @Override
    public List<BookingDto> getItemsBookings(long userId, String stateQuery, int from, int size) {
        userService.checkUser(userId);
        State state;
        Page<Booking> result = Page.empty();
        int offset = from > 0 ? from / size : 0;
        Pageable page = PageRequest.of(offset, size);
        try {
            state = State.valueOf(stateQuery.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + stateQuery);
        }
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, page);
                break;
            case PAST:
                result = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now, page);
                break;
            case FUTURE:
                result = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now, page);
                break;
            case CURRENT:
                result = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, now, now, page);
                break;
            case WAITING:
                result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                break;
            case REJECTED:
                result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                break;
        }
        return bookingMapper.toBookingDto(result);
    }
}
