package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {
    @EqualsAndHashCode.Include
    Long id;
    @Size(max = 30, message = "Длина названия не должна превышать 30 символов.")
    String name;
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов.")
    String description;
    Boolean available;
}
