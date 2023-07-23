package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ItemOwnerDto extends ItemDto {
    private BookingDtoShort lastBooking;
    private BookingDtoShort nextBooking;
    private Set<CommentDto> comments = new HashSet<>();
}