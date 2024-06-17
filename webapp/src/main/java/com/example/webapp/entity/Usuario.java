package com.example.webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = -3187669235052313317L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int id;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 45, message = "Los apellidos no puede tener más de 45 caracteres")
    private String nombres;

    @NotBlank
    @Size(max = 45, message = "Los apellidos no puede tener más de 45 caracteres")
    private String apellidos;

    @NotBlank(message = "El correo no puede quedar vacio")
    @Email(message = "Debe tener el formato tipo correo: example@example")
    @Size(max = 45, message = "El correo no puede tener más de 45 caracteres")
    private String correo;
    @Digits(integer = 8, fraction = 0, message = "El DNI debe tener exactamente 8 dígitos")
    @Positive(message = "El DNI debe ser un número positivo")
    private int dni;

    @NotBlank(message = "El codigo de colegiatura no puede quedar vacio")
    private String codigo_colegiatura;
    private int estado;
    private String contrasena;
    private byte[] foto;
    private Date fecha_creacion;
    private String estado_solicitud;
    private String motivo_rechazo;
    private int borrado_logico;
    @NotBlank(message = "La dirección no puede quedar vacia")
    @Size(max = 90, message = "La dirección no puede tener más de 90 caracteres")
    private String direccion;
    private String imagen;
    private String referencia;
    private String telefono;
    @ManyToOne
    @JoinColumn(name = "id_roles")
    private Roles rol;
    private int cuenta_activada;
    private Date fecha_recuperacion;
    private String token_recuperacion;
    private String punto;
    @OneToOne
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;
    @OneToOne
    @JoinColumn(name = "id_seguro")
    private Seguro seguro;

}
