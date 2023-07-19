package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoShort {
    private Long id;
    private Long bookerId;
    private LocalDateTime dateTime;
}