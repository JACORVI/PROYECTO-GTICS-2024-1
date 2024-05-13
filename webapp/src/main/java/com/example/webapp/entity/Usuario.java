package com.example.webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private int id;
    @Column(nullable = false)
    @NotBlank
    @Size( max = 45, message = "Los nombres no puede tener más de 45 caracteres")
    private String nombres;

    @NotBlank
    @Size( max = 45, message = "Los apellidos no puede tener más de 45 caracteres")
    private String apellidos;

    @NotBlank
    @Size( max = 45, message = "El correo no puede tener más de 45 caracteres")
    private String correo;

    @Positive(message = "El DNI debe ser un número positivo")
    @Max(value = 100000000, message = "El número de DNI debe tener 8 dígitos")
    @Min(value = 9999999, message = "El número de DNI debe tener 8 dígitos")
    private int dni;

    @NotBlank
    @Size( max = 45, message = "El código de colegiatura no puede tener más de 45 caracteres")
    private String codigo_colegiatura;

    @NotBlank
    @Size( max = 45, message = "El Distrito no puede tener más de 45 caracteres")
    private String distrito;
    private String seguro;
    private String estado;
    private String rol;

    @NotBlank
    @Size( max = 45, message = "El código de colegiatura no puede tener más de 45 caracteres")
    private String contrasena;
    private Date fecha_creacion;
    private String estado_solicitud;
    private String motivo_rechazo;
    private int borrado_logico;
}
