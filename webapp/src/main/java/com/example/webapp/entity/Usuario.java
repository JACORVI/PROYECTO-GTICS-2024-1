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

    @NotBlank(message = "El correo no puede quedar vacio")
    @Size(max = 45, message = "El correo no puede tener más de 45 caracteres")
    private String correo;

    private int dni;

    private String codigo_colegiatura;

    @NotNull(message = "Debe seleccionar un distrito")
    private String distrito;

    @NotNull(message = "Debe seleccionar una opción")
    private String seguro;

    private int estado;
    private String contrasena;
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
    private int cuenta_activada;
    private Date fecha_recuperacion;
    private String token_recuperacion;

    @ManyToOne
    @JoinColumn(name = "id_roles")
    private Roles rol;

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", correo=" + correo + ", dni=" + dni + ", codigo_colegiatura=" + codigo_colegiatura + ", distrito=" + distrito + ", seguro=" + seguro + ", estado=" + estado + ", contrasena=" + contrasena + ", fecha_creacion=" + fecha_creacion + ", estado_solicitud=" + estado_solicitud + ", motivo_rechazo=" + motivo_rechazo + ", borrado_logico=" + borrado_logico + ", direccion=" + direccion + ", imagen=" + imagen + ", referencia=" + referencia + ", telefono=" + telefono + ", cuenta_activada=" + cuenta_activada + ", fecha_recuperacion=" + fecha_recuperacion + ", token_recuperacion=" + token_recuperacion + ", rol=" + rol + '}';
    }

}
