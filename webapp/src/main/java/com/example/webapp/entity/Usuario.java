package com.example.webapp.entity;

import jakarta.persistence.*;
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
    private String nombres;
    private String apellidos;
    private String correo;
    private int dni;
    private String codigo_colegiatura;
    private String distrito;
    private String seguro;
    private String estado;
    private String rol;
    private String contrasena;
    private Date fecha_creacion;
    private String estado_solicitud;
}
