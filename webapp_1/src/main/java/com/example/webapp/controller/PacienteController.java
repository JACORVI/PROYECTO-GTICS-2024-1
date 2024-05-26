package com.example.webapp.controller;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
        List<PedidosPaciente> preordenList = pedidosPacienteRepository.buscarPedidosPorTipo("preorden");
        model.addAttribute("listaPreorden",preordenList);
        return "paciente/inicio";
    }
    /*---------------------------------------*/

    /*QRUD y vista de MEDICAMENTOS*/
    @GetMapping("/paciente/medicamentos")
    public String listarMedicamentos(Model model){
        int usuid = 29;

        List<Medicamentos> listamedicamentos = medicamentosRepository.findAll();
        model.addAttribute("listaMedicamentos",listamedicamentos);
        List<Carrito> tamanocarrito = carritoRepository.findAll();
        model.addAttribute("tamañoCarrito",tamanocarrito.size());

        //generador de numero de pedidos
        List<String> estadosdecompraporId = carritoRepository.estadosDeCompraPorUsuarioId(usuid);
        boolean soloEstadosRegistrados = true;

        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String numpedido = "";
        for (String palabra : estadosdecompraporId) {
            if (palabra!=null && palabra.equals("Comprando")) {
                soloEstadosRegistrados = false;
                break;
            }
        }
        if(estadosdecompraporId.isEmpty() || soloEstadosRegistrados){
            for (int x = 0; x < 12; x++) {
                int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
                char caracterAleatorio = banco.charAt(indiceAleatorio);
                numpedido += caracterAleatorio;
            }
        }
        model.addAttribute("numPedido",numpedido);

        return "paciente/medicamentos";
    }

    @PostMapping("/paciente/medicamentos/buscarMedicamentos")
    public String buscarMedicamentos(@RequestParam("searchField") String searchField,
                                     Model model){
        model.addAttribute("textoBuscado", searchField);
        List<Medicamentos> buscaMedicamentos = medicamentosRepository.buscarMedicamento(searchField);
        model.addAttribute("buscaMedicamentos", buscaMedicamentos);
        return "paciente/medicamentos";
    }

    @GetMapping("/paciente/añadirCarrito1")
    public String anadirMedicamentoAlCarrito1(Model model,
                                              @RequestParam("id") int id, @RequestParam("numpedido") String numpedido, RedirectAttributes attr){
        int usuid = 29;

        List<String> estadosdecompraporId = carritoRepository.estadosDeCompraPorUsuarioId(usuid);
        boolean soloEstadosRegistrados = true;
        for (String palabra : estadosdecompraporId) {
            if (palabra!=null && palabra.equals("Comprando")) {
                soloEstadosRegistrados = false;
                break;
            }
        }
        if(estadosdecompraporId.isEmpty() || soloEstadosRegistrados){
            if(estadosdecompraporId.isEmpty() || soloEstadosRegistrados){
                String registrado = "Registrado";
                carritoRepository.borrarPedidoRegistrado(usuid, registrado);
            }
            String estadocompra = "Comprando";
            int cantidad = 1;
            carritoRepository.AnadirAlCarrito(id, usuid, cantidad, numpedido, estadocompra);
            attr.addFlashAttribute("msg","Se agrego un nuevo producto al carrito!");
        }
        return "redirect:/paciente/medicamentos";
    }

    @GetMapping("/paciente/añadirCarrito2")
    public String anadirMedicamentoAlCarrito2(Model model,
                                              @RequestParam("id") int id, RedirectAttributes attr){
        int usuid = 29;
        String estadocompra = "Comprando";
        List<Carrito> duplicados = carritoRepository.buscarDuplicados(id);
        if (duplicados.isEmpty()){
            int cantidad = 1;
            List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(usuid);
            String numpedido = numeropedidoporId.get(0);
            carritoRepository.AnadirAlCarrito(id, usuid, cantidad, numpedido, estadocompra);
            attr.addFlashAttribute("msg","Se agrego un nuevo producto al carrito!");
        }
        else{
            int cantidadDelDuplicado = carritoRepository.cantidadDelDuplicado(id);
            int cantidad = cantidadDelDuplicado+1;
            int id1 = id;
            int usuid2 = usuid;
            List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(usuid);
            String numpedido = numeropedidoporId.get(0);
            carritoRepository.borrarElementoCarrito(id, usuid);
            carritoRepository.AnadirAlCarrito(id1, usuid2, cantidad, numpedido, estadocompra);
            attr.addFlashAttribute("msg","Se agrego un producto existente al carrito!");
        }
        return "redirect:/paciente/medicamentos";
    }

    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
    /*---------------------------------------*/



    /*QRUD y vista del CARRITO*/
    @GetMapping("/paciente/carrito")
    public String listarProductosCarritoRT(Model model){
        List<Carrito> listadodelcarritort = carritoRepository.findAll();
        int car = 0;
        if (listadodelcarritort.isEmpty()){
            String msg1 = "Su carrito esta vacio.";
            String msg2 = "No agrego ningun producto a su carrito.";
            car = 1;
            model.addAttribute("msg1",msg1);
            model.addAttribute("msg2",msg2);
            model.addAttribute("car",car);
        }
        else{
            car = 0;
            model.addAttribute("listadoDelCarrito",listadodelcarritort);
            List<Double> listaPrecioxCantidad = carritoRepository.CantidadxPrecioUnitario();
            double sumaTotal = 0.0;
            for (Double valor : listaPrecioxCantidad) {
                sumaTotal += valor;
            }
            String sumaTotal2D = String.format("%.2f", sumaTotal);
            model.addAttribute("precioTotal",sumaTotal2D);
            int delivery = 0;
            model.addAttribute("delivery",delivery);
            model.addAttribute("car",car);
        }
        return "paciente/carrito";
    }

    @GetMapping("/paciente/carrito/delivery")
    public String listarProductosCarritoDL(Model model){
        List<Carrito> listadodelcarritodl = carritoRepository.findAll();
        model.addAttribute("listadoDelCarrito",listadodelcarritodl);
        List<Double> listaPrecioxCantidad = carritoRepository.CantidadxPrecioUnitario();
        double sumaTotal = 0.0;
        for (Double valor : listaPrecioxCantidad) {
            sumaTotal += valor;
        }
        double sumaTotal1 = sumaTotal + 5.00;
        String sumaTotal2D = String.format("%.2f", sumaTotal);
        String sumaTotal2D1 = String.format("%.2f", sumaTotal1);
        model.addAttribute("precioTotal",sumaTotal2D);
        model.addAttribute("precioTotalDely",sumaTotal2D1);
        int delivery = 1;
        model.addAttribute("delivery",delivery);
        return "paciente/carrito";
    }

    @GetMapping("/paciente/carrito/borrar")
    public String borrarElementoCarrito(Model model,
                                        @RequestParam("id") int id) {
        int usuid = 29;
        carritoRepository.borrarElementoCarrito(id, usuid);
        return "redirect:/paciente/carrito";
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

    /*Vista de mensajes*/
    @GetMapping("/paciente/mensajes")
    public String listarMensajes(){
        return "paciente/mensajes";
    }

}