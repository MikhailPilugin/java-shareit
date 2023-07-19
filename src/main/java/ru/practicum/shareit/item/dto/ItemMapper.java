package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public List<ItemDto> toItemDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(toItemDto(item));
        }
        return result;
    }

    public ItemOwnerDto toOwnerDto(Item item, Collection<CommentDto> comments,
                                   Booking lastBooking, Booking nextBooking) {
        ItemOwnerDto dto = new ItemOwnerDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        BookingDtoShort simpleLastBookingDto = null;
        if (lastBooking != null) {
            simpleLastBookingDto = new BookingDtoShort(lastBooking.getId(), lastBooking.getBooker().getId(),
                    lastBooking.getEnd());
        }
        dto.setLastBooking(simpleLastBookingDto);
        BookingDtoShort simpleNextBookingDto = null;
        if (nextBooking != null) {
            simpleNextBookingDto = new BookingDtoShort(nextBooking.getId(), nextBooking.getBooker().getId(),
                    nextBooking.getStart());
        }
        dto.setNextBooking(simpleNextBookingDto);
        dto.getComments().addAll(comments);
        return dto;
    }

    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}

