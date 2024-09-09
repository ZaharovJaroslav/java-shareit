package ru.practicum.shareit.exception;

import org.apache.coyote.BadRequestException;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}