package com.baqterya.muzukanji.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.Character.UnicodeBlock;

import static com.baqterya.muzukanji.util.Util.isRomaji;

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
}
