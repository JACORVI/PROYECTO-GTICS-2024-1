package com.example.webapp.controller.paciente;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Medico;
import com.example.webapp.entity.Paciente;
import com.example.webapp.entity.Sede;
import com.example.webapp.repository.MedicamentosRepository;
import com.example.webapp.repository.MedicoRepository;
import com.example.webapp.repository.PacienteRepository;
import com.example.webapp.repository.SedeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


@Controller
public class PacienteController {

    /*Variables Final de los repository*/
    final
    PacienteRepository pacienteRepository;
    MedicamentosRepository medicamentosRepository;
    MedicoRepository medicoRepository;
    SedeRepository sedeRepository;
    public PacienteController(PacienteRepository pacienteRepository,
                              MedicamentosRepository medicamentosRepository,
                              MedicoRepository medicoRepository,
                              SedeRepository sedeRepository) {

        this.pacienteRepository = pacienteRepository;
        this.medicamentosRepository = medicamentosRepository;
        this.medicoRepository = medicoRepository;
        this.sedeRepository = sedeRepository;
    }
    /*---------------------------------------*/


    /*QRUD y vista de MEDICAMENTOS*/
    @GetMapping("/paciente/medicamentos")
    public String listarMedicamentos(Model model){
        List<Medicamentos> listamedicamentos = medicamentosRepository.findAll();
        model.addAttribute("listaMedicamentos",listamedicamentos);
        return "paciente/medicamentos";
    }
    /*---------------------------------------*/


    /*QRUD y vista del CARRITO*/
    @GetMapping("/paciente/carrito")
    public String listarProductosCarrito(Model model){

        return "paciente/carrito";
    }

    @GetMapping("/borrar")
    public String borrarElementoCarrito() {

        return "redirect:paciente/carrito";
    }
    /*---------------------------------------*/


    /*QRUD y vista del FORM*/
    @GetMapping("/paciente/form")
    public String listarMedicos(Model model){
        List<Medico> listamedicos = medicoRepository.findAll();
        model.addAttribute("listaMedicos",listamedicos);
        List<Sede> listasedes = sedeRepository.findAll();
        model.addAttribute("listaSedes",listasedes);
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