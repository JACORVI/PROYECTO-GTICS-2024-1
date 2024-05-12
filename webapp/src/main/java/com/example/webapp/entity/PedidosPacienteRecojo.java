package com.example.webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="pedidos_paciente_recojo")
public class PedidosPacienteRecojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idpedidos_paciente_recojo")
    private int id;
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede ser nulo")
    @Size(max = 45, message = "El nombre no puede tener más de 45 caracteres")
    private String nombre_paciente;
    @NotBlank(message = "El apellido no puede ser nulo")
    @Size(max = 45, message = "El apellido no puede tener más de 45 caracteres")
    private String apellido_paciente;
    private String medico_que_atiende;
    private String seguro;
    @Positive(message = "El número de celular debe ser positivo")
    @Max(value = 1000000000, message = "El número de celular debe tener 9 dígitos")
    @Min(value = 899999999, message = "El número de celular debe empezar con el dígito 9")
    private int telefono;
    @Positive(message = "El número de DNI debe ser positivo")
    @Max(value = 100000000, message = "El número de DNI debe tener 8 dígitos")
    @Min(value = 9999999, message = "El número de DNI debe tener 8 dígitos")
    private int dni;
    private double costo_total;
    private String tipo_de_pedido;
    private String fecha_solicitud;
    private String fecha_entrega;
    private String validacion_del_pedido;
    private String comentario;
    private String fecha_validacion;
    private String estado_del_pedido;
    private String numero_tracking;
    private String aviso_vencimiento;
    private String sede_de_recojo;
    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario")
    private Usuario usuario;
}
