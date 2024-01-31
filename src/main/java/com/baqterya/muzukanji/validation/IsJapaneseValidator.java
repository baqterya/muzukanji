package com.baqterya.muzukanji.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.Character.UnicodeBlock;

import static com.baqterya.muzukanji.util.Util.isJapanese;

public class IsJapaneseValidator implements ConstraintValidator<IsJapanese, String> {

    @Override
    public void initialize(IsJapanese constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String paramInput, ConstraintValidatorContext constraintValidatorContext) {
        if (paramInput == null) {
            return true;
        }
        return isJapanese(paramInput);
    }

}
