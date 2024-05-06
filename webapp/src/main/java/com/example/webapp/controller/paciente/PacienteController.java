package com.example.webapp.controller.paciente;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.PedidosPaciente;
import com.example.webapp.entity.Usuario;
import com.example.webapp.repository.MedicamentosRepository;
import com.example.webapp.repository.PedidosPacienteRepository;
import com.example.webapp.repository.UsuarioRepository;
import com.example.webapp.entity.Sede;
import com.example.webapp.repository.SedeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class PacienteController {

    /*Variables Final de los repository*/
    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;
    SedeRepository sedeRepository;
    PedidosPacienteRepository pedidosPacienteRepository;
    public PacienteController(MedicamentosRepository medicamentosRepository,
                              UsuarioRepository usuarioRepository,
                              SedeRepository sedeRepository,
                              PedidosPacienteRepository pedidosPacienteRepository) {

        this.medicamentosRepository = medicamentosRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidosPacienteRepository = pedidosPacienteRepository;
    }
    /*---------------------------------------*/

    /*Vista de inicio (lista de pre-ordenes)*/
    @GetMapping("/paciente/inicio")
    public String listarPreordenes(Model model){

        return "paciente/inicio";
    }

    /*QRUD y vista de MEDICAMENTOS*/
    @GetMapping("/paciente/medicamentos")
    public String listarMedicamentos(Model model){
        List<Medicamentos> listamedicamentos = medicamentosRepository.findAll();
        model.addAttribute("listaMedicamentos",listamedicamentos);
        return "paciente/medicamentos";
    }

    @PostMapping("/paciente/medicamentos/buscarMedicamentos")
    public String buscarMedicamentos(@RequestParam("searchField") String searchField,
                                     Model model){
        List<Medicamentos> buscaMedicamentos = medicamentosRepository.findByNombre(searchField);
        model.addAttribute("buscaMedicamentos", buscaMedicamentos);
        model.addAttribute("textoBuscado", searchField);
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
    @GetMapping("/paciente/carrito/nuevoPedido")
    public String formParaFinalizarCompra(  @ModelAttribute("pedidosPaciente") PedidosPaciente pedidosPaciente,
                                            Model model){
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        model.addAttribute("listaSedes",sedeRepository.findAll());
        return "paciente/formcompra";
    }
    @PostMapping("/paciente/guardar")
    public String guardarPedido(@ModelAttribute("pedidosPaciente") PedidosPaciente pedidosPaciente) {
        pedidosPacienteRepository.save(pedidosPaciente);
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