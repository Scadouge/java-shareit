package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommentDto {
    @EqualsAndHashCode.Include
    @Null
    Long id;

    @NotNull
    @Size(min = 1, max = 255, message = "Текст комментария должен быть в диапазоне 1-255 символов.")
    String text;

    @Null
    String authorName;

    @Null
    LocalDateTime created;
}


