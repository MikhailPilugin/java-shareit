package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemServiceImpl itemService;

    @Override
    public Booking add(Long userId, Booking booking) {
        booking.setBooker(userId);

        ItemDto itemDto = itemService.getById(userId, booking.getItemId());

        if (!itemDto.getAvailable()) {
            throw new RuntimeException("Item is not available");
        }

        return bookingRepository.save(booking);
    }
}
