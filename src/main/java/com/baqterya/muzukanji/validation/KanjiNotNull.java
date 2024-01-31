package com.baqterya.muzukanji.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = KanjiNotNullValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface KanjiNotNull {

    String message() default "Kanji cannot be null.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
