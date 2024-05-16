package ru.practicum.shareit.user.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = RequiredUserDataValidator.class)
@Target({ PARAMETER, FIELD })
@Retention(RUNTIME)
public @interface RequiredUserData {
    String message() default "Требуемые поля User отсутствуют";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
