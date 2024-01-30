package com.baqterya.muzukanji.validation;


import com.baqterya.muzukanji.model.KanjiDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

import static com.baqterya.muzukanji.util.Util.isJapanese;
import static com.baqterya.muzukanji.util.Util.isRomaji;

public class IsKanjiValidator implements ConstraintValidator<IsKanji, KanjiDto> {

    @Override
    public void initialize(IsKanji constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(KanjiDto value, ConstraintValidatorContext context) {
        if (value == null) return false;
        boolean validator = true;
        if (value.getKanji() != null) validator = isJapanese(value.getKanji());
        if (value.getKunyomi() != null) validator = isJapanese(value.getKunyomi());
        if (value.getKunyomiRomaji() != null) validator = isRomaji(value.getKunyomiRomaji());
        if (value.getOnyomi() != null) validator = isJapanese(value.getOnyomi());
        if (value.getOnyomiRomaji() != null) validator = isRomaji(value.getOnyomiRomaji());
        if (value.getJlptLevel() != null) validator = validateJlpt(value.getJlptLevel());
        return validator;
    }

    private boolean validateJlpt(String level) {
        if (level.length() != 2) return false;
        Character firstChar = level.charAt(0);
        int secondChar = Character.getNumericValue(level.charAt(1));
        if (!firstChar.equals('n') && !firstChar.equals('N')) return false;
        return secondChar >= 1 && secondChar <= 5;
    }
}
