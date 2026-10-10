package com.firearms.firearmcollectionjee.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for ValidCaliber annotation.
 * Validates that caliber value is within a realistic range for firearms.
 */
public class CaliberValidator implements ConstraintValidator<ValidCaliber, Double> {
    
    private double min;
    private double max;
    
    @Override
    public void initialize(ValidCaliber constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }
    
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull for null checks
        }
        return value >= min && value <= max;
    }
}
