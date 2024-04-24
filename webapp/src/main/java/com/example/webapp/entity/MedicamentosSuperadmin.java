package com.example.webapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medicamentos")
public class MedicamentosSuperadmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamentos", nullable = false)
    private int id_medicamentos;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "nombre")
    private String nombre;
    //@Column(name = "foto")
    //private boolean foto;
    @Column(name = "inventario")
    private String inventario;
    @Column(name = "precio_unidad")
    private String precio_unidad;
    @Column(name = "fecha_ingreso")
    private String fecha_ingreso;


    public int getId_medicamentos() {
        return id_medicamentos;
    }

    public void setId_medicamentos(int id_medicamentos) {
        this.id_medicamentos = id_medicamentos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*public boolean isFoto() {
        return foto;
    }

    public void setFoto(boolean foto) {
        this.foto = foto;
    }*/

    public String getInventario() {
        return inventario;
    }

    public void setInventario(String inventario) {
        this.inventario = inventario;
    }

    public String getPrecio_unidad() {
        return precio_unidad;
    }

    public void setPrecio_unidad(String precio_unidad) {
        this.precio_unidad = precio_unidad;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }
}