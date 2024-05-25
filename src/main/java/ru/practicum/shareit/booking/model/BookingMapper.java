package ru.practicum.shareit.booking.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "id", source = "bookingCreateDto.id")
    Booking toModel(BookingCreateDto bookingCreateDto, User booker, Item item);

    BookingDto toDto(Booking booking);

    List<BookingDto> toDto(List<Booking> bookings);
}
