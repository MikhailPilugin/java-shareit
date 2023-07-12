package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());

        return bookingDto;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        booking.setId(bookingDto.getId());
        booking.setBooker(bookingDto.getBooker());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        return booking;
    }
}
