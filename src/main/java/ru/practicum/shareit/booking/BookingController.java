package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    public final BookingService bookingService;

    @PostMapping
    public Booking add(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid
    @RequestBody Booking booking) {
//        LocalDate start = booking.getStart();
//        LocalDate end = booking.getEnd();



        return bookingService.add(userId, booking);
    }




    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(final RuntimeException e) {
        return new ErrorResponse("error", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        return new ErrorResponse("error", e.getMessage());
    }
}
