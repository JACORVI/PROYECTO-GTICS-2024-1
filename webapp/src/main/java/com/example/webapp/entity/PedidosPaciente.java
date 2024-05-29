package com.example.webapp.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.example.webapp.validation.ValidApellidos;
import com.example.webapp.validation.ValidNombre;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
@Table(name="pedidos_paciente")
public class PedidosPaciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idpedidos_paciente")
    private int id;
    @Column(nullable = false)
    @ValidNombre
    private String nombre_paciente;
    @ValidApellidos
    private String apellido_paciente;
    private String medico_que_atiende;
    private String seguro;
    @NotBlank(message = "La dirección no puede ser nula")
    @Size(max = 90, message = "La dirección no puede tener más de 90 caracteres")
    private String direccion;
    @NotBlank(message = "El distrito no debe ser nulo")
    private String distrito;
    @Positive(message = "El número de celular debe ser positivo")
    @Max(value = 1000000000, message = "El número de celular debe tener 9 dígitos")
    @Min(value = 899999999, message = "El número de celular debe empezar con el dígito 9")
    private int telefono;
    @Positive(message = "El número de DNI debe ser positivo")
    @Max(value = 100000000, message = "El número de DNI debe tener 8 dígitos")
    @Min(value = 9999999, message = "El número de DNI debe tener 8 dígitos")
    private int dni;
    @NotBlank(message = "La hora de entrega no puede ser nula")
    private String hora_de_entrega;
    private Double costo_total;
    private String tipo_de_pedido;
    private String fecha_solicitud;
    private String fecha_entrega;
    private String validacion_del_pedido;
    private String comentario;
    private String fecha_validacion;
    private String estado_del_pedido;
    private String numero_tracking;
    private String aviso_vencimiento;
    private String metodo_pago;
    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario")
    private Usuario usuario;
}
