package com.example.webapp.entity;

import javax.persistence.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.example.webapp.validation.ValidNombre;
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
    @ValidNombre
    private String nombres;
    @NotBlank
    @Size( max = 45, message = "Los apellidos no puede tener más de 45 caracteres")
    private String apellidos;

    @NotBlank
    @Size( max = 45, message = "El correo no puede tener más de 45 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$", message = "El correo debe tener un formato válido")
    private String correo;

    @Digits(integer = 8, fraction = 0, message = "El DNI debe tener exactamente 8 dígitos")
    @Positive(message = "El DNI debe ser un número positivo")
    private int dni;

   // @NotBlank
   // @Size( max = 45, message = "El código de colegiatura no puede tener más de 45 caracteres")
    private String codigo_colegiatura;

    @NotBlank
    @Size( max = 45, message = "El Distrito no puede tener más de 45 caracteres")
    private String distrito;
    private String seguro;
    private String estado;
    private String rol;

   // @NotBlank
   // @Size( max = 45, message = "El código de colegiatura no puede tener más de 45 caracteres")
    private String contrasena;
    private Date fecha_creacion;
    private String estado_solicitud;
    private String motivo_rechazo;
    private int borrado_logico;


    private String imagen;
    private String telefono;
    private String referencia;
    private String direccion;
}
