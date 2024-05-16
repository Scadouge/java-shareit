package ru.practicum.shareit.item.validation;

import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredItemDataValidator implements ConstraintValidator<RequiredItemData, ItemDto> {
    @Override
    public boolean isValid(ItemDto itemDto, ConstraintValidatorContext context) {
        return itemDto.getAvailable() != null && itemDto.getName() != null && itemDto.getDescription() != null
                && !itemDto.getDescription().isBlank() && !itemDto.getName().isBlank();
    }

    @Override
    public void initialize(RequiredItemData constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
