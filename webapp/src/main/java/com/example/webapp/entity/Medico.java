package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="medico")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_medico")
    private int idmedico;
    @Column(nullable = false)
    private String nombre;
    private String apellido;
    private int sede_id_sede;
    private String colegiatura_medico;
}
