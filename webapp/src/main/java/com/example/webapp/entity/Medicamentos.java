package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="paciente")
public class Medicamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_paciente")
    private int id_medicamentos;
    @Column(nullable = false)
    private String descripcion;
    private String nombre;
    private String inventario;
    private String precio_unidad;
    private String fecha_ingreso;
}