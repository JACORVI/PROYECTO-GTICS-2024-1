package com.example.webapp.controller.paciente;

import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class PacienteController {

    /*Variables Final de los repository*/
    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;
    SedeRepository sedeRepository;
    PedidosPacienteRepository pedidosPacienteRepository;
    CarritoRepository carritoRepository;

    public PacienteController(MedicamentosRepository medicamentosRepository,
                              UsuarioRepository usuarioRepository,
                              SedeRepository sedeRepository,
                              PedidosPacienteRepository pedidosPacienteRepository,
                              CarritoRepository carritoRepository) {

        this.medicamentosRepository = medicamentosRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidosPacienteRepository = pedidosPacienteRepository;
        this.carritoRepository = carritoRepository;
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
        List<Carrito> tamañocarrito = carritoRepository.findAll();
        model.addAttribute("tamañoCarrito",tamañocarrito.size());
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

    @GetMapping("/paciente/añadirCarrito")
    public String añadirMedicamentoAlCarrito(Model model,
                                             @RequestParam("id") int id, RedirectAttributes attr){
        int usuid = 29;

        List<Carrito> carrito = carritoRepository.buscarDuplicados(id);

        if (carrito.isEmpty()){
            int cantidad = 1;
            carritoRepository.AñadirAlCarrito(id, usuid, cantidad);
            attr.addFlashAttribute("msg","Se agrego un nuevo producto al carrito!");
        }
        else{
            int cantidadDelDuplicado = carritoRepository.cantidadDelDuplicado(id);
            int cantidad = cantidadDelDuplicado+1;
            int id1 = id;
            int usuid2 = usuid;
            carritoRepository.borrarDuplicado(id, usuid);
            carritoRepository.AñadirAlCarrito(id1, usuid2, cantidad);
            attr.addFlashAttribute("msg","Se agrego un producto existente al carrito!");
        }
        return "redirect:/paciente/medicamentos";

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