package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookingForItemExtendDto {
    @EqualsAndHashCode.Include
    Long id;

    Long bookerId;

    BookingStatus status;

    LocalDateTime start;

    LocalDateTime end;
}
