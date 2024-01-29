package com.baqterya.muzukanji.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.Character.UnicodeBlock;

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
        return IsJapanese(paramInput);
    }

    private boolean IsJapanese(String input) {
        boolean isJapanese = true;
        String inputWithoutPunctuation = input.replaceAll("\\p{P}", "");
        for (char c : inputWithoutPunctuation.toCharArray()) {
            UnicodeBlock block = (UnicodeBlock.of(c));
            if (
                block != UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                && block != UnicodeBlock.HIRAGANA
                && block != UnicodeBlock.KATAKANA
                && block != UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                && block != UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                && c != ' '
            ) {
                isJapanese = false;
                break;
            }
        }
        return isJapanese;
    }
}
