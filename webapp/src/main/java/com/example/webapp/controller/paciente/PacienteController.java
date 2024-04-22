package com.example.webapp.controller.paciente;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @GetMapping("/medicamentos")
    public String listarMedicamentos(){
        return "paciente/medicamentos";
    }

    @GetMapping("/carrito")
    public String listarProductosCarrito(){
        return "paciente/carrito";
    }

    @GetMapping("/carrito/form")
    public String formParaFinalizarCompra(){
        return "paciente/carrito";
    }

    @GetMapping("/carrito/msg")
    public String finalmsg(){
        return "paciente/finalmsgcompra";
    }



}