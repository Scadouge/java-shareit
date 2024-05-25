package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.validation.ValidBookingInterval;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ValidBookingInterval(message = "Некорректное время бронирования")
public class BookingCreateDto {
    @EqualsAndHashCode.Include
    @Null
    Long id;

    @NotNull(message = "Id предмета не может отсутствовать")
    Long itemId;

    @Null
    BookingStatus status;

    @NotNull(message = "Дата начала бронирования не может отсутствовать")
    LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования не может отсутствовать")
    LocalDateTime end;
}
