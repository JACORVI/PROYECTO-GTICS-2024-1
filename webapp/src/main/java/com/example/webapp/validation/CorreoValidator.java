package com.example.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorreoValidator implements ConstraintValidator<ValidCorreo, String> {

    @Override
    public void initialize(ValidCorreo validCorreo) {
        // Este método se puede usar para inicializar el validador en caso de ser necesario.
    }

    @Override
    public boolean isValid(String correo, ConstraintValidatorContext context) {
        if (correo == null || correo.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El correo no puede estar vacío")
                    .addConstraintViolation();
            return false;
        }
        if (correo.length() > 30) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El correo debe tener menos de 30 caracteres")
                    .addConstraintViolation();
            return false;
        }
        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El correo debe tener un formato válido")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
