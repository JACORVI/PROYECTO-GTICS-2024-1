package com.example.webapp.entity;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Medicamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_medicamentos")
    private int idmedicamentos;
    @Column(nullable = false)
    private String descripcion;
    private String nombre;
    private int foto;
    private String inventario;
    private int precio_unidad;
    private String fecha_ingreso;
}
