package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoShortTest {

    @Autowired
    JacksonTester<BookingDtoShort> json;

    @Test
    @SneakyThrows
    void testDto() {
        BookingDtoShort bookingDtoShort = new BookingDtoShort();
        Long bookingId = 1L;
        bookingDtoShort.setId(bookingId);
        Long bookerId = 2L;
        bookingDtoShort.setBookerId(bookerId);
        LocalDateTime dateTime = LocalDateTime.of(2023, 7, 6, 20, 20, 50);
        bookingDtoShort.setDateTime(dateTime);

        JsonContent<BookingDtoShort> result = json.write(bookingDtoShort);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingId.intValue());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(bookerId.intValue());
        assertThat(result).extractingJsonPathValue("$.dateTime").isEqualTo(dateTime.toString());
    }
}