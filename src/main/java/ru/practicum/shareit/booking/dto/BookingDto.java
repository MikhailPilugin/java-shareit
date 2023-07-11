package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Status;
import ru.practicum.shareit.user.User;

import javax.persistence.Column;
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
