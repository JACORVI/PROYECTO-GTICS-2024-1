package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="pedidos_reposicion")
public class PedidosReposicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_pedidos_reposicion")
    private int id;
    @Column(nullable = false)
    private Date fecha_solicitud;
    private double costo_total;
    private Date fecha_entrega;
    private String estado_de_reposicion;
}
