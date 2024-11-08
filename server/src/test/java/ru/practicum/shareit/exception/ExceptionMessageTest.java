package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class  ExceptionMessageTest {

    @Test
    void test_NotFoundExceptionMessage() {
        String expectedMessage = "Не найдено";
        NotFoundException exception = new NotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void test_ValidationExceptionMessage() {
        String expectedMessage = "Ошибка валидации";
        ValidationException exception = new ValidationException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void test_BadRequestExceptionMessage() {
        String expectedMessage = "Некорректный запрос";
        BadRequestException exception = new BadRequestException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void test_NotAvailableExceptionMessage() {
        String expectedMessage = "Ошибка";
        NotAvailableException exception = new NotAvailableException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void test_OperationAccessExceptionMessage() {
        String expectedMessage = "Ошибка доступа к операции";
        OperationAccessException exception = new OperationAccessException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void test_ConflictExceptionMessage() {
        String expectedMessage = "Конфликт данных";
        ConflictException exception = new ConflictException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void test_TimeDataExceptionMessage() {
        String expectedMessage = "Ошибка Даты и Времени";
        TimeDataException exception = new TimeDataException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
