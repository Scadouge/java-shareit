package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utils.Headers;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(Headers.HEADER_USER_ID) Long userId,
                                    @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        log.info("Создание бронирования booking={}, userId={}", bookingCreateDto, userId);
        return bookingService.createBooking(userId, bookingCreateDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(Headers.HEADER_USER_ID) Long userId,
                                 @PathVariable Long bookingId) {
        log.info("Получение информации о бронировании bookingId={}, userId={}", bookingId, userId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> getBookingsByState(@RequestHeader(Headers.HEADER_USER_ID) Long userId,
                                                     @RequestParam(required = false) String state) {
        log.info("Получение списка бронирований пользователя userId={}, фильтр по state={}", userId, state);
        return bookingService.getBookingsForUser(userId, BookingState.parseState(state));
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingsByStateOwner(@RequestHeader(Headers.HEADER_USER_ID) Long userId,
                                                          @RequestParam(required = false) String state) {
        log.info("Получение списка бронирований владельцем userId={}, фильтр по state={}", userId, state);
        return bookingService.getBookingsForItems(userId, BookingState.parseState(state));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(Headers.HEADER_USER_ID) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam boolean approved) {
        log.info("Обновлении статуса бронировании userId={}, bookingId={}, approved={}", userId, bookingId, approved);
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }
}
