package com.example.taskmanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    int minLength() default 8;

    String message() default "Password must be at least {minLength} characters long and contain both letters and numbers";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

