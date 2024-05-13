package com.example.webapp.controller.farmacista;

import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller


public class FarmacistaController {
    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;
    SedeRepository sedeRepository;
    PedidosPacienteRepository pedidosPacienteRepository;
    CarritoRepository carritoRepository;

    public FarmacistaController(MedicamentosRepository medicamentosRepository,
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
    @GetMapping("/farmacista/medicamentos")
    public String listarMedicamentosFarmacista(Model model){
        int farmacistaId = 26; // Cambia esto al ID del farmacista actual

        List<Medicamentos> listaMedicamentos = medicamentosRepository.findAll();
        model.addAttribute("listaMedicamentos", listaMedicamentos);

        List<Carrito> tamanoCarrito = carritoRepository.listarCarrito();
        model.addAttribute("tamañoCarrito", tamanoCarrito.size());

        // Generador de número de pedidos
        List<String> estadosDeCompraPorId = carritoRepository.estadosDeCompraPorUsuarioId(farmacistaId);
        boolean soloEstadosRegistrados = true;

        String banco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String numPedido = "";
        for (String estado : estadosDeCompraPorId) {
            if (estado != null && estado.equals("Comprando")) {
                soloEstadosRegistrados = false;
                break;
            }
        }
        if(estadosDeCompraPorId.isEmpty() || soloEstadosRegistrados){
            for (int x = 0; x < 12; x++) {
                if (x > 0 && x % 4 == 0) {
                    String guion = "-";
                    numPedido += guion;
                }
                int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
                char caracterAleatorio = banco.charAt(indiceAleatorio);
                numPedido += caracterAleatorio;
            }
        }
        model.addAttribute("numPedido", numPedido);

        return "farmacista/medicamentos";
    }
    //@PostMapping("/buscarPorNombre")
    //public String buscarPorNombre(@RequestParam("searchField") String searchField, Model model) {
    //    List<Medicamentos> lista = medicamentosRepository.buscarPorNombreODescripcion(searchField);
    //    model.addAttribute("listaMedicamentos", lista);
    //    model.addAttribute("textoBuscado", searchField);
    //    return "farmacista/medicamentos";
    //}



    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }




    @GetMapping("/farmacista/añadirCarrito1")
    public String anadirMedicamentoAlCarrito1(Model model,
                                              @RequestParam("id") int id, @RequestParam("numpedido") String numpedido, RedirectAttributes attr){
        int usuid = 26;

        List<String> estadosdecompraporId = carritoRepository.estadosDeCompraPorUsuarioId(usuid);
        boolean soloEstadosRegistrados = true;
        for (String palabra : estadosdecompraporId) {
            if (palabra!=null && palabra.equals("Comprando")) {
                soloEstadosRegistrados = false;
                break;
            }
        }
        if(estadosdecompraporId.isEmpty() || soloEstadosRegistrados){
            if(soloEstadosRegistrados){
                String registrado = "Registrado";
                carritoRepository.borrarPedidoRegistrado(usuid, registrado);
            }
            String estadocompra = "Comprando";
            int cantidad = 1;
            carritoRepository.AnadirAlCarrito(id, usuid, cantidad, numpedido, estadocompra);
            attr.addFlashAttribute("msg","Se agrego un nuevo producto al carrito!");
        }
        return "redirect:/farmacista/medicamentos";
    }

    @GetMapping("/farmacista/añadirCarrito2")
    public String anadirMedicamentoAlCarrito2(Model model,
                                              @RequestParam("id") int id, RedirectAttributes attr){
        int usuid = 26;
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
        return "redirect:/farmacista/medicamentos";
    }



    @GetMapping("/farmacista/carrito")
    public String listarProductosCarritoRT(Model model) {
        int usuid = 26;
        List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(usuid);
        if (!numeropedidoporId.isEmpty()){
            String numpedido = numeropedidoporId.get(0);
            model.addAttribute("numpedido", numpedido);
        }
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
        return "farmacista/carrito";
    }

    @GetMapping("/farmacista/carrito/delivery")
    public String listarProductosCarritoDL(Model model){
        int usuid = 26;
        List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(usuid);
        if (!numeropedidoporId.isEmpty()){
            String numpedido = numeropedidoporId.get(0);
            model.addAttribute("numpedido", numpedido);
        }
        List<Carrito> listadodelcarritodl = carritoRepository.listarCarrito();
        int car = 0;
        if (listadodelcarritodl.isEmpty()){
            String msg1 = "Su carrito esta vacio.";
            String msg2 = "No agrego ningun producto a su carrito.";
            car = 1;
            model.addAttribute("msg1",msg1);
            model.addAttribute("msg2",msg2);
            model.addAttribute("car",car);
        }
        else{
            model.addAttribute("listadoDelCarrito",listadodelcarritodl);
            List<Double> listaPrecioxCantidad = carritoRepository.CantidadxPrecioUnitario();
            double sumaTotal = 0.0;
            for (Double valor : listaPrecioxCantidad) {
                sumaTotal += valor;
            }
            double sumaTotal1 = sumaTotal;
            String sumaTotal2D = String.format("%.2f", sumaTotal);
            String sumaTotal2D1 = String.format("%.2f", sumaTotal1);
            model.addAttribute("precioTotal",sumaTotal2D);
            model.addAttribute("precioTotalDely",sumaTotal2D1);
            int delivery = 1;
            model.addAttribute("delivery",delivery);
            model.addAttribute("car",car);
        }
        return "farmacista/carrito";
    }

    @GetMapping("/farmacista/carrito/borrar")
    public String borrarElementoCarrito(Model model,
                                        @RequestParam("id") int id) {
        int usuid = 26;
        carritoRepository.borrarElementoCarrito(id, usuid);
        return "redirect:/farmacista/carrito";
    }

    @GetMapping("/farmacista/carrito/registrarPedido")
    public String registrarPedido(Model model,
                                  @RequestParam("costototal") double costototal, @RequestParam("tipopedido") int tipo, @RequestParam("numtrack") String numtrack){
        int usuid = 26;
        String tipopedido = "Web - Recojo en tienda";
        if(tipo == 1){
            tipopedido = "Web - Delivery";
            String validacionpedido = "Pendiente";
            String estadopedido = "Registrando";
            carritoRepository.registrarPedidoDely(costototal, tipopedido, validacionpedido, estadopedido, numtrack, usuid);
            return "redirect:/farmacista/carrito/nuevoPedidoDelivery";
        }
        else{
            String validacionpedido = "Pendiente";
            String estadopedido = "Registrando";
            carritoRepository.registrarPedidoReco(costototal, tipopedido, validacionpedido, estadopedido, numtrack, usuid);
            return "redirect:/farmacista/carrito/nuevoPedidoRecojo";
        }

    }
    /*---------------------------------------*/



    /*QRUD y vista del FORM*/
    @GetMapping("/farmacista/carrito/nuevoPedidoDelivery")
    public String formParaFinalizarCompraDely(@ModelAttribute("pedidosPaciente") PedidosPaciente pedidosPaciente,
                                              Model model){
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        return "farmacista/formcompradely";
    }

    @GetMapping("/farmacista/carrito/nuevoPedidoRecojo")
    public String formParaFinalizarCompraRecojo(@ModelAttribute("pedidosPacienteRecojo") PedidosPacienteRecojo pedidosPacienteRecojo,
                                                Model model){
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        model.addAttribute("listasedes", sedeRepository.findAll());
        return "farmacista/formcompra";
    }

    @PostMapping("/farmacista/guardarDely")
    public String guardarPedidoDely(@ModelAttribute("pedidosPaciente") @Valid PedidosPaciente pedidosPaciente, BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("listausuarios", usuarioRepository.findAll());
            return "farmacista/formcompradely";
        }
        else{
            String nombre = pedidosPaciente.getNombre_paciente();
            String apellido = pedidosPaciente.getApellido_paciente();
            int dni = pedidosPaciente.getDni();
            int telefono = pedidosPaciente.getTelefono();
            String seguro = pedidosPaciente.getSeguro();
            String medico = pedidosPaciente.getMedico_que_atiende();
            String vencimiento = pedidosPaciente.getAviso_vencimiento();

            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechasoli = fechaActual.format(formatter);

            String direccion = pedidosPaciente.getDireccion();
            String distrito = pedidosPaciente.getDistrito();
            String horaentrega = pedidosPaciente.getHora_de_entrega();
            String estadopedido = "Registrado";
            int usuid = 26;
            carritoRepository.finalizarPedido1(nombre,apellido,dni,telefono,seguro,medico,vencimiento,fechasoli,direccion,distrito,horaentrega,estadopedido,usuid);
            carritoRepository.borrarCarritoPorId(usuid);

            return "redirect:/farmacista/medicamentos";
        }
    }

    @PostMapping("/farmacista/guardarRecojo")
    public String guardarPedidoReco(@ModelAttribute("pedidosPacienteRecojo") @Valid PedidosPacienteRecojo pedidosPacienteRecojo,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listausuarios", usuarioRepository.findAll());
            model.addAttribute("listasedes", sedeRepository.findAll());
            return "farmacista/formcompra";
        } else {
            // Obtener los datos del formulario
            String nombre = pedidosPacienteRecojo.getNombre_paciente();
            String apellido = pedidosPacienteRecojo.getApellido_paciente();
            int dni = pedidosPacienteRecojo.getDni();
            String sederecojo = pedidosPacienteRecojo.getSede_de_recojo();
            String estadopedido = "Registrado";
            int usuid = 26;
            // Actualizar el pedido con los datos del formulario
            carritoRepository.finalizarPedido22(nombre, apellido, dni, sederecojo, usuid);

            // Borrar el carrito por el ID del usuario
            carritoRepository.borrarCarritoPorId(usuid);

            return "redirect:/farmacista/medicamentos";
        }
    }




    @GetMapping("farmacista/cancelarRegistroPedidoDely")
    public String cancelarRegistroDePedidoDely(){
        int usuid = 26;
        carritoRepository.cancelarPedidoDely(usuid);
        return "redirect:/farmacista/medicamentos";
    }

    @GetMapping("farmacista/cancelarRegistroPedidoReco")
    public String cancelarRegistroDePedidoReco(){
        int usuid = 26;
        carritoRepository.cancelarPedidoReco(usuid);
        return "redirect:/farmacista/medicamentos";
    }
    /*---------------------------------------*/



}
