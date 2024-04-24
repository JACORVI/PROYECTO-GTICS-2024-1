package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="pedidos")
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_ventas")
    private int id;
    @Column(nullable = false)
    private int usuario_id_usuario;
    private String fecha_solicitud;
    private double costo_total;
    private String estado_del_pedido;
    private String tipo;
    private String fecha_entrega;
    private String comentario;
    private String fecha_aprobacion;
    private String medico_que_atiende;
    private String envios_id_envios;
}
