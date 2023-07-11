package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.Status;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class BookingDto {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private Long item;
    private Long booker;
    private Status status;
}
