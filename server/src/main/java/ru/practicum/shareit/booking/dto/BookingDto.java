package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    //@NotNull
    //@FutureOrPresent
    private LocalDateTime start;
    //@NotNull
   //@FutureOrPresent
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;
}
