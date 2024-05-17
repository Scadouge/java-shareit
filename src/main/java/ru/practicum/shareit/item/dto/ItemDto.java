package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.validation.ValidationGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {
    @EqualsAndHashCode.Include
    Long id;
    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Size(min = 2, message = "Длина названия не должна быть меньше 2 символов.")
    @Size(max = 30, message = "Длина названия не должна превышать 30 символов.")
    String name;
    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Size(min = 2, message = "Длина описания не должна быть меньше 2 символов.")
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов.")
    String description;
    @NotNull(groups = ValidationGroup.OnCreate.class)
    Boolean available;
}
