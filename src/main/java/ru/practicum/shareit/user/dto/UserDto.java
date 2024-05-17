package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.validation.ValidationGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDto {
    @EqualsAndHashCode.Include
    Long id;
    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Size(min = 2, message = "Имя не должно быть меньше 2 символов.")
    @Size(max = 30, message = "Имя не должно быть больше 30 символов.")
    String name;
    @NotNull(groups = ValidationGroup.OnCreate.class)
    @Size(min = 1, message = "Неверный формат почты")
    @Email(message = "Неверный формат почты")
    String email;
}
