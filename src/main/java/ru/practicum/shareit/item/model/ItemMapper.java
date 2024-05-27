package ru.practicum.shareit.item.model;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingForItemExtendDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toDto(Item item);

    @Mapping(target = "requestId", ignore = true)
    Item toModel(ItemDto itemDto, Long ownerId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "requestId", ignore = true)
    void updateModel(@MappingTarget Item item, ItemDto updaterItemDto);

    @Mapping(target = "id", source = "item.id")
    ItemExtendDto toItemBookingInfoDto(Item item, List<CommentDto> comments,
                                       BookingForItemExtendDto lastBooking, BookingForItemExtendDto nextBooking);
}
