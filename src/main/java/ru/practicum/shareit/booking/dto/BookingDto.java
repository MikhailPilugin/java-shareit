package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long userId;
    private String status;
}
