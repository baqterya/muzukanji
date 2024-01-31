package com.baqterya.muzukanji.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = IsKanjiValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsKanji {

    String message() default "The data in the payload is not a valid kanji.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
