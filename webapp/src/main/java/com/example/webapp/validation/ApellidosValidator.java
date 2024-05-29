package com.example.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ApellidosValidator implements ConstraintValidator<ValidApellidos, String> {

    @Override
    public void initialize(ValidApellidos validApellidos) {
        // Este método se puede usar para inicializar el validador en caso de ser necesario.
    }

    @Override
    public boolean isValid(String apellidos, ConstraintValidatorContext context) {
        if (apellidos == null || apellidos.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Los apellidos no pueden estar vacíos")
                    .addConstraintViolation();
            return false;
        }
        if (apellidos.length() > 30) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Los apellidos deben tener menos de 30 caracteres")
                    .addConstraintViolation();
            return false;
        }
        if (!apellidos.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Los apellidos solo pueden contener letras y espacios")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
