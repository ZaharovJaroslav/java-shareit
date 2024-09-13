package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
     private final BookingService bookingService;

     @PostMapping
     public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody NewBookingRequestDto bookingRequest) {
         log.debug("Добавление нового бронирования");
         if (bookingRequest == null) {
            throw new NotFoundException("Новое бронирование не указано");
         }
         return bookingService.addBooking(userId,bookingRequest);
    }

     @PatchMapping("/{bookingId}")
     public BookingDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam Boolean approved) {
         log.debug("Одобрение бронирования инструмента Арендодателем");
         return bookingService.approve(userId,bookingId,approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId) {
        log.debug("Получение бронирования по id");
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Получение всех бронировний пользователя");
        return bookingService.findAllByUserId(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Получение списка Арендованных инсрументов пользователя");
        return bookingService.findAllBookingsByOwner(userId, state);
    }
}
