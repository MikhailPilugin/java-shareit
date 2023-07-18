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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        } else if (itemDto.getOwner() == userId) {
            throw new IllegalArgumentException("Attempt to booking from owner");
        }

        item = ItemMapper.toItem(itemDto);
        itemService.getById(itemDto.getOwner(), item.getId());

        item.setOwner(userService.getUser(userId));

        booking = BookingMapper.toBooking(bookingDto);

        booking.setBooker(user);
        booking.setItem(item);

        booking.setStatus(Status.valueOf(bookingDto.getStatus()));

        bookingRepository.save(booking);

        return booking;
    }

    @Override
    public Collection<Booking> getAll(Long userId, String state) {
        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("User not found");
        }

        Collection<Booking> bookingCollection;

        if (state == null) {
            return bookingCollection = bookingRepository.findAllByBookerIdOrderByStatusDesc(userId);
        }

        switch (state) {
            case "ALL":
                return bookingCollection = bookingRepository.findAllByBookerIdOrderByStatusDesc(userId);
            case "FUTURE":
                return bookingCollection = bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStatusDesc(userId, LocalDateTime.now());
            default:
                throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public Booking getById(Long userId, Long bookingId) {
        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Booking> bookingList = new ArrayList<>();

        Optional<Booking> optionalBooking = bookingRepository.findBookingById(bookingId);

        if (!optionalBooking.isPresent()) {
            throw new IllegalArgumentException("Booking not found");
        }

        if (optionalBooking.get().getBooker().getId() != userId) {
            throw new IllegalArgumentException("Wrong booker id");
        }

        return optionalBooking.get();
    }

    @Override
    public Collection<Booking> getByOwner(Long userId, String state) {
        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Booking> bookingList;

        if (state == null) {
            return bookingList = bookingRepository.findBookingByItemOwnerIdOrderByStatusDesc(userId);
        } else {
            switch (state) {
                case "ALL":
                    return bookingList = bookingRepository.findBookingByItemOwnerIdOrderByStatusDesc(userId);
                case "FUTURE":
                    return bookingList = bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStatusDesc(userId, LocalDateTime.now());
                default:
                    throw new RuntimeException("Unknown state: UNSUPPORTED_STATUS");
            }
        }
    }


    @Override
    public Booking update(Long userId, Long bookingId, Boolean approved) {
        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("User not found");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking = optionalBooking.get();

//        Long id = booking.getItem().getOwner().getId();

        boolean st = booking.getStatus().equals(Status.APPROVED);

        if (approved != null) {
            if(booking.getItem().getOwner().getId() == userId && approved == true && !booking.getStatus().equals(Status.APPROVED)) {
                booking.setStatus(Status.APPROVED);
            } else if (booking.getItem().getOwner().getId() == userId && approved == false) {
                booking.setStatus(Status.REJECTED);
            } else if (booking.getItem().getOwner().getId() != userId) {
                throw new IllegalArgumentException("Attempt to change status by user");
            } else if (approved == true && booking.getStatus().equals(Status.APPROVED)) {
                throw new RuntimeException("Attempt to change exist status");
            }
        } else if (booking.getItem().getOwner().getId() != userId) {
            throw new RuntimeException("Attempt to change status by user");
        }


//        else {
//            throw new RuntimeException("Wrong booking id");
//        }


        bookingRepository.save(booking);

        return booking;
    }
}
