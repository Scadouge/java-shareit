package ru.practicum.shareit.item.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = RequiredItemDataValidator.class)
@Target({ PARAMETER, FIELD })
@Retention(RUNTIME)
public @interface RequiredItemData {
    String message() default "Требуемые поля Item отсутствуют";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
