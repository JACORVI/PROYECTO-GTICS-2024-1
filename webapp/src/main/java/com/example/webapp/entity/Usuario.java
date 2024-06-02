package com.example.webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name="usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private int id;
    @Column(nullable = false)

    @NotBlank
    @Size( max = 45, message = "Los apellidos no puede tener más de 45 caracteres")
    private String nombres;
    @NotBlank
    @Size( max = 45, message = "Los apellidos no puede tener más de 45 caracteres")
    private String apellidos;

    @NotBlank
    @Size( max = 45, message = "El correo no puede tener más de 45 caracteres")
    private String correo;

    @Digits(integer = 8, fraction = 0, message = "El DNI debe tener exactamente 8 dígitos")
    @Positive(message = "El DNI debe ser un número positivo")
    private int dni;

    @NotBlank
    @Size( max = 45, message = "El código de colegiatura no puede tener más de 45 caracteres")
    private String codigo_colegiatura;

    @NotBlank
    @Size( max = 45, message = "El Distrito no puede tener más de 45 caracteres")
    private String distrito;
    private String seguro;
    private int estado;
    private String contrasena;
    private Date fecha_creacion;
    private String estado_solicitud;
    private String motivo_rechazo;
    private int borrado_logico;

    private String direccion;
    private String imagen;
    private String referencia;
    private String telefono;

    @ManyToOne
    @JoinColumn(name="id_roles")
    private Roles rol;
}
