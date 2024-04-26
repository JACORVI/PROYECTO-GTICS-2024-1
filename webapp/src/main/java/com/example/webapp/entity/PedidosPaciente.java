package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
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
    private String medico_que_atiende;
    private String seguro;
    private String direccion;
    private String distrito;
    private int telefono;
    private int dni;
    private Time hora_de_entrega;
    private Double costo_total;
    private String tipo_de_pedido;
    private Date fecha_solicitud;
    private Date fecha_entrega;
    private String validacion_del_pedido;
    private String comentario;
    private Date fecha_validacion;
    private String estado_del_pedido;
}
