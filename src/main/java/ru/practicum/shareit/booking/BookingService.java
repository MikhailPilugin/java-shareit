package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

public interface BookingService {

    BookingDto add(long userId, BookingDto bookingDto);

    BookingDto update(long userId, long bookingId, String approved);

    BookingDto getById(long bookingId, long userId);

    List<BookingDto> getAll(long userId, String state);

    List<BookingDto> getByOwner(long userId, String state);
}
