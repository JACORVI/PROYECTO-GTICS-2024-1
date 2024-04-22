package com.example.webapp.entity;

import jakarta.persistence.*;
import java

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_paciente")
    private int idpaciente;
    @Column(nullable = false)
    private String seguro;
    private String edad;
    private int usuario_id_usuario;
    private String telefono;
}
