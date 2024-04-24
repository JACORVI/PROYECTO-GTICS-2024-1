package com.example.webapp.controller.paciente;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Pedidos;
import com.example.webapp.entity.Usuario;
import com.example.webapp.repository.MedicamentosRepository;
import com.example.webapp.repository.PedidosRepository;
import com.example.webapp.repository.UsuarioRepository;
import com.example.webapp.entity.Sede;
import com.example.webapp.repository.SedeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class PacienteController {

    /*Variables Final de los repository*/
    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;
    SedeRepository sedeRepository;
    PedidosRepository pedidosRepository;
    public PacienteController(MedicamentosRepository medicamentosRepository,
                              UsuarioRepository usuarioRepository,
                              SedeRepository sedeRepository,
                              PedidosRepository pedidosRepository) {

        this.medicamentosRepository = medicamentosRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidosRepository = pedidosRepository;
    }
    /*---------------------------------------*/

    /*Vista de inicio (lista de pre-ordenes)*/
    @GetMapping("/paciente/inicio")
    public String listarPreordenes(Model model){
        List<Pedidos> listapreordenes = pedidosRepository.findByTipo("preorden");
        model.addAttribute("listaPreordenes",listapreordenes);
        return "paciente/inicio";
    }

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
    public String formParaFinalizarCompra( Model model){
        List<Usuario> listausuarios = usuarioRepository.findAll();
        model.addAttribute("listausuarios", listausuarios);
        List<Sede> listasedes = sedeRepository.findAll();
        model.addAttribute("listaSedes",listasedes);
        return "paciente/formcompra";
    }
    @PostMapping("/paciente/guardar")
    public String guardarPedido(Pedidos pedidos) {
        pedidosRepository.save(pedidos);
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