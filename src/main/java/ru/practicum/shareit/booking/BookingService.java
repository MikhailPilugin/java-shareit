package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {

//    Collection<Booking> getAll(Long userId);
//
//    Booking getById(Long userId, Long itemId);
//
//    List<Booking> search(Long userId, String text);

    Booking add(Long userId, BookingDto booking);

//    Booking update(long itemId, Booking item, long userId);
}
