package com.baqterya.muzukanji.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.Character.UnicodeBlock;

public class IsRomajiValidator implements ConstraintValidator<IsRomaji, String> {
    @Override
    public void initialize(IsRomaji constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String paramInput, ConstraintValidatorContext constraintValidatorContext) {
        if (paramInput == null) {
            return true;
        }
        return isRomaji(paramInput);
    }

    private boolean isRomaji(String input) {
        boolean isRomaji = true;
        for (char c : input.toCharArray()) {
            if (UnicodeBlock.of(c) != UnicodeBlock.BASIC_LATIN) {
                isRomaji = false;
                break;
            }
        }
        return isRomaji;
    }
}
