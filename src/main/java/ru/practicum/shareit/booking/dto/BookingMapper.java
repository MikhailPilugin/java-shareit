package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public Booking toBooking(BookingDto bookingDto) {
        Booking entity = new Booking();
        entity.setStart(bookingDto.getStart());
        entity.setEnd(bookingDto.getEnd());
        return entity;
    }

    public BookingDto toBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        if (booking.getBooker() != null) {
            dto.setBooker(userMapper.toUserDto(booking.getBooker()));
        }
        if (booking.getItem() != null) {
            dto.setItem(itemMapper.toItemDto(booking.getItem()));
        }
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public List<BookingDto> toBookingDto(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingDto(booking));
        }
        return result;
    }
}
