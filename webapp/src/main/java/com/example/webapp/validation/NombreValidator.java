package com.example.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NombreValidator implements ConstraintValidator<ValidNombre, String> {

    @Override
    public void initialize(ValidNombre validNombre) {
    }

    @Override
    public boolean isValid(String nombre, ConstraintValidatorContext context) {
        if (nombre == null || nombre.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El nombre no puede estar vacío").addConstraintViolation();
            return false;
        }
        if (nombre.length() > 45) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El nombre debe tener menos de 45 caracteres").addConstraintViolation();
            return false;
        }
        if (!nombre.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El nombre solo puede contener letras").addConstraintViolation();
            return false;
        }
        return true;
    }
}
