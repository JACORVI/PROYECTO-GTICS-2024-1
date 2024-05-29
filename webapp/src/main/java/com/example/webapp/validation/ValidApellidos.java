package com.example.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ApellidosValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidApellidos {
    String message() default "Los apellidos no cumplen con los requisitos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
