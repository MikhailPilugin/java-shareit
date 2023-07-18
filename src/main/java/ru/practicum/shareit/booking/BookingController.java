package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    public final BookingService bookingService;

    @PostMapping
    public Booking add(@RequestHeader("X-Sharer-User-Id") Long userId,
    @RequestBody BookingDto booking) {
        return bookingService.add(userId, booking);
    }

    @GetMapping
    public Collection<Booking> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestParam(required = false) String state) {
        return bookingService.getAll(userId, state);
    }

    @GetMapping("/{bookingId}")
    public Booking getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping("/owner")
    public Collection<Booking> getByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(required = false) String state) {
        return bookingService.getByOwner(userId, state);
    }


    @PatchMapping("/{bookingId}")
    public Booking update(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestParam(required = false) Boolean approved, @PathVariable Long bookingId) {
        return bookingService.update(userId, bookingId, approved);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(final RuntimeException e) {
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }
}
