package com.baqterya.muzukanji.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = IsRomajiValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsRomaji {
    String message() default "This parameter requires Latin Characters (Romaji).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
