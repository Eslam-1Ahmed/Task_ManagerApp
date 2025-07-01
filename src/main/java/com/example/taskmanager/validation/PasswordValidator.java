package com.example.taskmanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword,String> {

    private int minLength;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{" + minLength + ",}$";
        return value.matches(pattern);
    }

}
