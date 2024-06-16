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
    private String nombres;
    private String apellidos;
    private int dni;
    private String codigo_colegiatura;
    private int estado;
    private String contrasena;
    private String punto;
    private Date fecha_creacion;
    private String estado_solicitud;
    private String motivo_rechazo;
    private int borrado_logico;
    private String imagen;
    private String referencia;
    private String telefono;
    private int cuenta_activada;
    private Date fecha_recuperacion;
    private String token_recuperacion;

    @NotBlank(message = "El correo no puede quedar vacio")
    @Email(message = "Debe tener el formato tipo correo: example@example")
    @Size(max = 45, message = "El correo no puede tener más de 45 caracteres")
    private String correo;

    @NotBlank(message = "La dirección no puede quedar vacia")
    @Size(max = 90, message = "La dirección no puede tener más de 90 caracteres")
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_roles")
    private Roles rol;

    @OneToOne
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;

    @OneToOne
    @JoinColumn(name = "id_seguro")
    private Seguro seguro;

}
