package ru.practicum.shareit.user.validation;

import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredUserDataValidator implements ConstraintValidator<RequiredUserData, UserDto> {
    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
        return userDto.getEmail() != null && userDto.getName() != null && !userDto.getName().isBlank();
    }

    @Override
    public void initialize(RequiredUserData constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
