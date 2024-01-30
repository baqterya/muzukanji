package com.baqterya.muzukanji.validation;

import com.baqterya.muzukanji.model.KanjiDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

public class KanjiNotNullValidator implements ConstraintValidator<KanjiNotNull, KanjiDto> {

    @Override
    public void initialize(KanjiNotNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(KanjiDto value, ConstraintValidatorContext context) {
        return value.getKanji() != null;
    }
}
