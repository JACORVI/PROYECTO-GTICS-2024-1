package com.example.webapp.controller.paciente;

import com.example.webapp.repository.MedicamentosRepository;
import com.example.webapp.repository.MedicoRepository;
import com.example.webapp.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class PacienteController {

    /*Variables Final de los repository*/
    final
    PacienteRepository pacienteRepository;
    MedicamentosRepository medicamentosRepository;
    MedicoRepository medicoRepository;
    public PacienteController(PacienteRepository pacienteRepository, MedicamentosRepository medicamentosRepository, MedicoRepository medicoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicamentosRepository = medicamentosRepository;
        this.medicoRepository = medicoRepository;
    }
    /*---------------------------------------*/


    /*QRUD y vista de MEDICAMENTOS*/
    @GetMapping("/paciente/medicamentos")
    public String listarMedicamentos(){

        return "paciente/medicamentos";

    }
    /*---------------------------------------*/


    /*QRUD y vista del CARRITO*/
    @GetMapping("/paciente/carrito")
    public String listarProductosCarrito(){

        return "paciente/carrito";
    }

    @GetMapping("/borrar")
    public String borrarElementoCarrito() {

        return "redirect:paciente/carrito";
    }
    /*---------------------------------------*/


    /*QRUD y vista del FORM*/
    @GetMapping("/paciente/carrito/form")
    public String formParaFinalizarCompra(){

        return "paciente/formcompra";
    }
    @PostMapping("/guardar")
    public String guardarPedido() {

        return "redirect:/paciente/finalmsgcompra";
    }
    /*---------------------------------------*/


    /*QRUD y vista de MIS PEDIDOS*/
    @GetMapping("/paciente/mispedidos")
    public String listaPedidos(){

        return "paciente/mispedidos";
    }
    /*---------------------------------------*/


    /*QRUD y vista de ESTADO DEL PEDIDO*/
    @GetMapping("/paciente/mispedidos/estadopedido")
    public String estadoTrack(){

        return "paciente/estadotrck";
    }
    /*---------------------------------------*/

}