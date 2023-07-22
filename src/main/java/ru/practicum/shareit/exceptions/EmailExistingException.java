package ru.practicum.shareit.exceptions;

public class EmailExistingException extends RuntimeException {
    public EmailExistingException(String message) {
        super(message);
    }
}
