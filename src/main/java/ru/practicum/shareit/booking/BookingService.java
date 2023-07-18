package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {

    Collection<Booking> getAll(Long userId, String state);

    Booking getById(Long userId, Long bookingId);
//
//    List<Booking> search(Long userId, String text);

    Collection<Booking> getByOwner(Long userId, String state);

    Booking add(Long userId, BookingDto booking);

    Booking update(Long userId, Long bookingId, Boolean approved);
}
