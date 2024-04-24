package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="medicamentos")
public class Medicamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_medicamentos")
    private int id;
    @Column(nullable = false)
    private String descripcion;
    private String nombre;
    private String inventario;
    private double precio_unidad;
    private String fecha_ingreso;
}