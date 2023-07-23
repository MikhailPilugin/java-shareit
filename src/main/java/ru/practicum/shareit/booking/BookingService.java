package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto add(long userId, BookingDto bookingDto);

    BookingDto update(long userId, long bookingId, String approved);

    BookingDto getById(long bookingId, long userId);

    List<BookingDto> getUserBookings(long userId, String state, int from, int size);

    List<BookingDto> getItemsBookings(long userId, String state, int from, int size);
}
