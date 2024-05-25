package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.validation.ValidationGroup;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {
    @EqualsAndHashCode.Include
    @Null(groups = ValidationGroup.OnCreate.class)
    Long id;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Size(min = 2, max = 30, message = "Длина названия должна быть в диапазоне 2-30 символов.")
    String name;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Size(min = 2, max = 200, message = "Длина описания должна быть в диапазоне 2-200 символов.")
    String description;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    Boolean available;
}
