package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingShortDto {
    private Long id;
//    @NotNull
//    @FutureOrPresent
    private LocalDateTime start;
//    @NotNull
//    @FutureOrPresent
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
}