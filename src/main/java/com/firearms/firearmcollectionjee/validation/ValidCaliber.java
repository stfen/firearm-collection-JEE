package com.firearms.firearmcollectionjee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validation annotation for firearm caliber.
 * Ensures caliber is within realistic range (1.0 to 40.0 mm).
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CaliberValidator.class)
@Documented
public @interface ValidCaliber {
    String message() default "Caliber must be between 1.0 and 40.0 mm";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    double min() default 1.0;
    double max() default 40.0;
}
