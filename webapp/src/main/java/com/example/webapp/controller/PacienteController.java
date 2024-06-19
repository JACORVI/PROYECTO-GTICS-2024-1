package com.example.webapp.controller;

import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    PedidosPacienteRecojoRepository pedidosPacienteRecojoRepository;
    MedicamentosRecojoRepository medicamentosRecojoRepository;
    MedicamentosDelPedidoRepository medicamentosDelPedidoRepository;
    SeguroRepository seguroRepository;
    DistritoRepository distritoRepository;

    public PacienteController(MedicamentosRepository medicamentosRepository,
                              UsuarioRepository usuarioRepository,
                              SedeRepository sedeRepository,
                              PedidosPacienteRepository pedidosPacienteRepository,
                              CarritoRepository carritoRepository,
                              PedidosPacienteRecojoRepository pedidosPacienteRecojoRepository,
                              MedicamentosRecojoRepository medicamentosRecojoRepository,
                              MedicamentosDelPedidoRepository medicamentosDelPedidoRepository,
                              SeguroRepository seguroRepository,
                              DistritoRepository distritoRepository) {

        this.medicamentosRepository = medicamentosRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidosPacienteRepository = pedidosPacienteRepository;
        this.carritoRepository = carritoRepository;
        this.pedidosPacienteRecojoRepository = pedidosPacienteRecojoRepository;
        this.medicamentosRecojoRepository = medicamentosRecojoRepository;
        this.medicamentosDelPedidoRepository = medicamentosDelPedidoRepository;
        this.seguroRepository = seguroRepository;
        this.distritoRepository = distritoRepository;
    }
    /*---------------------------------------*/


    /*QRUD y vista de PREORDENES*/
    @GetMapping(value = {"/paciente/inicio", "/paciente/", "/paciente"})
    public String listarPreordenes(Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
            carritoRepository.cancelarPedidoDely(usuid);
        }
        carritoRepository.cancelarPedidoReco(usuid);

        List<PedidosPaciente> listaPedidosPreorden = pedidosPacienteRepository.pedidosPreorden(usuid);

        model.addAttribute("listaPedidosPreorden", listaPedidosPreorden);
        model.addAttribute("tamanolistaPreOrden", listaPedidosPreorden.size());
        model.addAttribute("ultimosMedicamentos", medicamentosRepository.ultimosMedicamentos());
        int lleno = 1;
        if (listaPedidosPreorden.isEmpty()) {
            lleno = 0;
        }
        model.addAttribute("lleno", lleno);
        return "paciente/inicio";
    }
    @GetMapping("/paciente/generarPreorden")
    public String registrarPreorden(@RequestParam("id") int id, Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idped = carritoRepository.idPedidoRegistrando(usuid);
        if(idped != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idped);
            carritoRepository.cancelarPedidoDely(usuid);
        }
        carritoRepository.cancelarPedidoReco(usuid);
        String estadopedido = "Registrando";
        List<Integer> precioDelMedicamento = carritoRepository.precioDelMedicamento(id);
        double costototal = precioDelMedicamento.get(0);
        String tipopedido = "Pre-orden";
        String validacion = "Pendiente";
        String banco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String numpedido = "";

        //generador de numero de pedidos
        boolean i = true;
        List<String> lista1 = pedidosPacienteRepository.numerosDePedidosDely();
        List<String> lista2 = pedidosPacienteRecojoRepository.numerosDePedidosReco();
        List<String> lista3 = carritoRepository.numerosDePedidosCarrito();
        int duplicado = 0;
        while (i){
            for (int x = 0; x < 12; x++) {
                if (x > 0 && x % 4 == 0) {
                    String guion = "-";
                    numpedido += guion;
                }
                int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
                char caracterAleatorio = banco.charAt(indiceAleatorio);
                numpedido += caracterAleatorio;
            }
            for (String palabra : lista1) {
                if (palabra.equals(numpedido)) {
                    duplicado++;
                }
            }
            for (String palabra : lista2) {
                if (palabra.equals(numpedido)) {
                    duplicado++;
                }
            }
            for (String palabra : lista3) {
                if (palabra.equals(numpedido)) {
                    duplicado++;
                }
            }
            if(duplicado == 0){
                break;
            }
        }
        carritoRepository.registrarPedidoDely(costototal, tipopedido, validacion, estadopedido, numpedido, usuid);
        List<Integer> listidpedidodely = carritoRepository.idpedidoPorUsuIdDely(usuid);
        int idpedido = listidpedidodely.get(0);
        List<String> listanombre = carritoRepository.nombreDelMedicamento(id);
        String nombre = listanombre.get(0);
        int cantidad = 1;
        carritoRepository.registrarMedicamentosPedidoPreorden(id, nombre, costototal, cantidad, idpedido, usuid);

        return "redirect:/paciente/preordenForm";
    }
    @GetMapping("/paciente/preordenForm")
    public String preordenFormRegistro(@ModelAttribute("pedidosPaciente") PedidosPaciente pedidosPaciente,
                                       Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        model.addAttribute("nombres", usuario.getNombres());
        model.addAttribute("apellidos", usuario.getApellidos());
        model.addAttribute("dni", usuario.getDni());
        model.addAttribute("seguro", usuario.getSeguro().getNombre());
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        model.addAttribute("listaDistritos", distritoRepository.findAll());
        return "paciente/formcomprapreorden";
    }
    @PostMapping("/paciente/guardarPreorden")
    private String guardarPreorden(@ModelAttribute("pedidosPaciente") @Valid PedidosPaciente pedidosPaciente, BindingResult bindingResult,
                                   Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        if (bindingResult.hasErrors() || pedidosPaciente.getDistrito().equals("") || pedidosPaciente.getMedico_que_atiende().equals("") || pedidosPaciente.getAviso_vencimiento().equals("")){
            if (pedidosPaciente.getDistrito().equals("")){
                model.addAttribute("distritoError", "Debe seleccionar el distrito del lugar de la entrega");
            }
            if (pedidosPaciente.getMedico_que_atiende().equals("")){
                model.addAttribute("medicoError", "Debe seleccionar una opción");
            }
            if (pedidosPaciente.getAviso_vencimiento().equals("")){
                model.addAttribute("avisoError", "Debe seleccionar una opción");
            }
            model.addAttribute("nombres", usuario.getNombres());
            model.addAttribute("apellidos", usuario.getApellidos());
            model.addAttribute("dni", usuario.getDni());
            model.addAttribute("seguro", usuario.getSeguro().getNombre());
            model.addAttribute("listausuarios", usuarioRepository.findAll());
            model.addAttribute("listaDistritos", distritoRepository.findAll());
            return "paciente/formcompradely";
        }
        else{
            String nombre = usuario.getNombres();
            String apellido = usuario.getApellidos();
            int dni = usuario.getDni();
            String seguro = usuario.getSeguro().getNombre();
            int telefono = pedidosPaciente.getTelefono();
            String medico = pedidosPaciente.getMedico_que_atiende();
            String vencimiento = pedidosPaciente.getAviso_vencimiento();

            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechasoli = fechaActual.format(formatter);

            String direccion = pedidosPaciente.getDireccion();
            String distrito = pedidosPaciente.getDistrito();
            String horaentrega = pedidosPaciente.getHora_de_entrega();
            String estadopedido = "Pendiente";
            int usuid = usuario.getId();
            List<String> listidNumtrack = carritoRepository.idNumTrackPorUsuIdDely(usuid);
            String numTrack = listidNumtrack.get(0);

            carritoRepository.finalizarPedido1(nombre,apellido,dni,telefono,seguro,medico,vencimiento,fechasoli,direccion,distrito,horaentrega,estadopedido,usuid);

            model.addAttribute("numTracking", numTrack);
            model.addAttribute("preorden", 1);

            return "paciente/finalmsgCompra";
        }
    }
    @GetMapping("/paciente/cancelarRegistroPedidoPreorden")
    public String cancelarRegistroDePedidoPreorden(Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        List<Integer> listaid = carritoRepository.idpedidoRegistrandoPreorden(usuid);
        int id = listaid.get(0);
        carritoRepository.borrarMedicamentosAlCancelar(usuid,id);
        carritoRepository.cancelarPedidoDely(usuid);
        return "redirect:/paciente/medicamentos";
    }
    /*---------------------------------------*/

    /*QRUD y vista de MEDICAMENTOS*/
    @GetMapping("/paciente/medicamentos")
    public String listarMedicamentos(Model model, Authentication authentication){

        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();

        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
                carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
                carritoRepository.cancelarPedidoDely(usuid);
        }

        carritoRepository.cancelarPedidoReco(usuid);
        String banco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String numpedido = "";

        List<Carrito> carrito = carritoRepository.carritoPorId(usuid);

        model.addAttribute("listaMedicamentos",medicamentosRepository.findAll());
        model.addAttribute("cantidadMedicamentos",medicamentosRepository.findAll().size());
        model.addAttribute("carrito", carrito.size());

        //generador de numero de pedidos
        List<String> estadosdecompraporId = carritoRepository.estadosDeCompraPorUsuarioId(usuid);
        boolean soloEstadosRegistrados = true;
        for (String palabra : estadosdecompraporId) {
            if (palabra!=null && palabra.equals("Comprando")) {
                soloEstadosRegistrados = false;
                break;
            }
        }
        if(estadosdecompraporId.isEmpty() || soloEstadosRegistrados){
            boolean i = true;
            List<String> lista1 = pedidosPacienteRepository.numerosDePedidosDely();
            List<String> lista2 = pedidosPacienteRecojoRepository.numerosDePedidosReco();
            List<String> lista3 = carritoRepository.numerosDePedidosCarrito();
            int duplicado = 0;
            while (i){
                for (int x = 0; x < 12; x++) {
                    if (x > 0 && x % 4 == 0) {
                        String guion = "-";
                        numpedido += guion;
                    }
                    int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
                    char caracterAleatorio = banco.charAt(indiceAleatorio);
                    numpedido += caracterAleatorio;
                }
                for (String palabra : lista1) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                for (String palabra : lista2) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                for (String palabra : lista3) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                if(duplicado == 0){
                    break;
                }
            }
        }
        model.addAttribute("numPedido",numpedido);
        int principal = 1;
        model.addAttribute("principal", principal);

        List<String> listaCategorias = medicamentosRepository.listaCategorias();
        Set<String> setCategorias = new HashSet<>(listaCategorias);
        List<String> listaSinDuplicados = new ArrayList<>(setCategorias);
        model.addAttribute("listaCategorias", listaSinDuplicados);

        return "paciente/medicamentos";
    }

    @PostMapping("/paciente/medicamentos/buscador")
    public String buscarMedicamentos(@RequestParam("searchField") String searchField,
                                    Authentication authentication, Model model){
        if(searchField.equals("")){
            return "redirect:/paciente/medicamentos";
        }

        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
            carritoRepository.cancelarPedidoDely(usuid);
        }
        carritoRepository.cancelarPedidoReco(usuid);
        String banco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String numpedido = "";

        List<Carrito> carrito = carritoRepository.carritoPorId(usuid);

        model.addAttribute("listaMedicamentos",medicamentosRepository.buscarMedicamento(searchField));
        model.addAttribute("cantidadMedicamentos",medicamentosRepository.findAll().size());
        model.addAttribute("carrito", carrito.size());

        //generador de numero de pedidos
        List<String> estadosdecompraporId = carritoRepository.estadosDeCompraPorUsuarioId(usuid);
        boolean soloEstadosRegistrados = true;
        for (String palabra : estadosdecompraporId) {
            if (palabra!=null && palabra.equals("Comprando")) {
                soloEstadosRegistrados = false;
                break;
            }
        }
        if(estadosdecompraporId.isEmpty() || soloEstadosRegistrados){
            boolean i = true;
            List<String> lista1 = pedidosPacienteRepository.numerosDePedidosDely();
            List<String> lista2 = pedidosPacienteRecojoRepository.numerosDePedidosReco();
            List<String> lista3 = carritoRepository.numerosDePedidosCarrito();
            int duplicado = 0;
            while (i){
                for (int x = 0; x < 12; x++) {
                    if (x > 0 && x % 4 == 0) {
                        String guion = "-";
                        numpedido += guion;
                    }
                    int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
                    char caracterAleatorio = banco.charAt(indiceAleatorio);
                    numpedido += caracterAleatorio;
                }
                for (String palabra : lista1) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                for (String palabra : lista2) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                for (String palabra : lista3) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                if(duplicado == 0){
                    break;
                }
            }
        }
        model.addAttribute("numPedido",numpedido);
        int principal = 0;
        model.addAttribute("principal", principal);

        List<String> listaCategorias = medicamentosRepository.listaCategorias();
        Set<String> setCategorias = new HashSet<>(listaCategorias);
        List<String> listaSinDuplicados = new ArrayList<>(setCategorias);
        model.addAttribute("listaCategorias", listaSinDuplicados);

        return "paciente/medicamentos";
    }

    @PostMapping("/paciente/medicamentos/filtrar")
    public String filtrarMedicamentos(@RequestParam("filtroCategoria") String categoria, @RequestParam("filtroOrden") String orden,
                                      Authentication authentication, Model model){

        if(categoria.equals("") && orden.equals("")){
            System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAA");
            return "redirect:/paciente/medicamentos";
        }
        System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAA " + orden);
        if(!categoria.equals("")){
            if(!orden.equals("")) {
                if (orden.equals("1")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro1(categoria);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "mayor a menor precio");
                }
                if (orden.equals("2")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro2(categoria);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "menor a mayor precio");
                }
                if (orden.equals("3")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro3(categoria);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "antiguo a nuevo");
                }
                if (orden.equals("4")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro4(categoria);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "A -> Z");
                }
                if (orden.equals("5")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro5(categoria);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "Z -> A");
                }
            }
            else{
                List<Medicamentos> medicamentosPorCategoria = medicamentosRepository.listaMedicamentosPorCategoria(categoria);
                model.addAttribute("listaMedicamentos", medicamentosPorCategoria);
                model.addAttribute("categoria", categoria);
            }
        }
        else{
            if (orden.equals("1")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro1();
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("orden", "mayor a menor precio");
            }
            if (orden.equals("2")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro2();
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("orden", "menor a mayor precio");
            }
            if (orden.equals("3")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro3();
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("orden", "antiguo a nuevo");
            }
            if (orden.equals("4")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro4();
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("orden", "A -> Z");
            }
            if (orden.equals("5")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro5();
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("orden", "Z -> A");
            }
        }
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
            carritoRepository.cancelarPedidoDely(usuid);
        }
        carritoRepository.cancelarPedidoReco(usuid);
        String banco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String numpedido = "";

        List<Carrito> carrito = carritoRepository.carritoPorId(usuid);

        model.addAttribute("cantidadMedicamentos",medicamentosRepository.findAll().size());
        model.addAttribute("carrito", carrito.size());

        //generador de numero de pedidos
        List<String> estadosdecompraporId = carritoRepository.estadosDeCompraPorUsuarioId(usuid);
        boolean soloEstadosRegistrados = true;
        for (String palabra : estadosdecompraporId) {
            if (palabra!=null && palabra.equals("Comprando")) {
                soloEstadosRegistrados = false;
                break;
            }
        }
        if(estadosdecompraporId.isEmpty() || soloEstadosRegistrados){
            boolean i = true;
            List<String> lista1 = pedidosPacienteRepository.numerosDePedidosDely();
            List<String> lista2 = pedidosPacienteRecojoRepository.numerosDePedidosReco();
            List<String> lista3 = carritoRepository.numerosDePedidosCarrito();
            int duplicado = 0;
            while (i){
                for (int x = 0; x < 12; x++) {
                    if (x > 0 && x % 4 == 0) {
                        String guion = "-";
                        numpedido += guion;
                    }
                    int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
                    char caracterAleatorio = banco.charAt(indiceAleatorio);
                    numpedido += caracterAleatorio;
                }
                for (String palabra : lista1) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                for (String palabra : lista2) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                for (String palabra : lista3) {
                    if (palabra.equals(numpedido)) {
                        duplicado++;
                    }
                }
                if(duplicado == 0){
                    break;
                }
            }
        }
        model.addAttribute("numPedido",numpedido);
        int principal = 0;
        model.addAttribute("principal", principal);

        List<String> listaCategorias = medicamentosRepository.listaCategorias();
        Set<String> setCategorias = new HashSet<>(listaCategorias);
        List<String> listaSinDuplicados = new ArrayList<>(setCategorias);
        model.addAttribute("listaCategorias", listaSinDuplicados);

        return "paciente/medicamentos";
    }

    @GetMapping("/paciente/medicamentos/info")
    public String informacionDelMedicamento(){
        return "paciente/infomedicamento";
    }

    @GetMapping("/paciente/añadirCarrito1")
    public String anadirMedicamentoAlCarrito1(Model model, Authentication authentication,
                                              @RequestParam("id") int id, @RequestParam("numpedido") String numpedido, RedirectAttributes attr){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();

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
    public String anadirMedicamentoAlCarrito2(Model model, Authentication authentication,
                                              @RequestParam("id") int id, RedirectAttributes attr){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();

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
    public String listarProductosCarritoRT(Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
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
        return "paciente/carrito";
    }
    @GetMapping("/paciente/carrito/delivery")
    public String listarProductosCarritoDL(Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
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
            double sumaTotal1 = sumaTotal + 10.00;
            String sumaTotal2D = String.format("%.2f", sumaTotal);
            String sumaTotal2D1 = String.format("%.2f", sumaTotal1);
            model.addAttribute("precioTotal",sumaTotal2D);
            model.addAttribute("precioTotalDely",sumaTotal2D1);
            int delivery = 1;
            model.addAttribute("delivery",delivery);
            model.addAttribute("car",car);
        }
        return "paciente/carrito";
    }

    @GetMapping("/paciente/carrito/borrar")
    public String borrarElementoCarrito(Model model, Authentication authentication,
                                        @RequestParam("id") int id) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        carritoRepository.borrarElementoCarrito(id, usuid);
        return "redirect:/paciente/carrito";
    }

    @GetMapping("/paciente/carrito/registrarPedido")
    public String registrarPedido(Model model, Authentication authentication,
                                  @RequestParam("costototal") double costototal, @RequestParam("tipopedido") int tipo, @RequestParam("numtrack") String numtrack){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
            carritoRepository.cancelarPedidoDely(usuid);
        }
        carritoRepository.cancelarPedidoReco(usuid);
        String tipopedido = "Web - Recojo en tienda";
        if(tipo == 1){
            tipopedido = "Web - Delivery";
            String validacionpedido = "Pendiente";
            String estadopedido = "Registrando";
            carritoRepository.registrarPedidoDely(costototal, tipopedido, validacionpedido, estadopedido, numtrack, usuid);
            return "redirect:/paciente/carrito/nuevoPedidoDelivery";
        }
        else{
            String validacionpedido = "Pendiente";
            String estadopedido = "Registrando";
            carritoRepository.registrarPedidoReco(costototal, tipopedido, validacionpedido, estadopedido, numtrack, usuid);
            return "redirect:/paciente/carrito/nuevoPedidoRecojo";
        }

    }
    /*---------------------------------------*/



    /*QRUD y vista del FORM*/
    @GetMapping("/paciente/carrito/nuevoPedidoDelivery")
    public String formParaFinalizarCompraDely(@ModelAttribute("pedidosPaciente") PedidosPaciente pedidosPaciente,
                                              Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        model.addAttribute("nombres", usuario.getNombres());
        model.addAttribute("apellidos", usuario.getApellidos());
        model.addAttribute("dni", usuario.getDni());
        model.addAttribute("seguro", usuario.getSeguro().getNombre());
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        model.addAttribute("listaDistritos", distritoRepository.findAll());
        return "paciente/formcompradely";
    }

    @GetMapping("/paciente/carrito/nuevoPedidoRecojo")
    public String formParaFinalizarCompraRecojo(@ModelAttribute("pedidosPacienteRecojo") PedidosPacienteRecojo pedidosPacienteRecojo,
                                                Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        model.addAttribute("nombres", usuario.getNombres());
        model.addAttribute("apellidos", usuario.getApellidos());
        model.addAttribute("dni", usuario.getDni());
        model.addAttribute("seguro", usuario.getSeguro().getNombre());
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        model.addAttribute("listasedes", sedeRepository.findAll());
        return "paciente/formcompra";
    }

    @PostMapping("/paciente/guardarDely")
    public String guardarPedidoDely(@ModelAttribute("pedidosPaciente") @Valid PedidosPaciente pedidosPaciente, BindingResult bindingResult,
                                    Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        if (bindingResult.hasErrors() || pedidosPaciente.getDistrito().equals("") || pedidosPaciente.getMedico_que_atiende().equals("") || pedidosPaciente.getAviso_vencimiento().equals("")){
            if (pedidosPaciente.getDistrito().equals("")){
                model.addAttribute("distritoError", "Debe seleccionar el distrito del lugar de la entrega");
            }
            if (pedidosPaciente.getMedico_que_atiende().equals("")){
                model.addAttribute("medicoError", "Debe seleccionar una opción");
            }
            if (pedidosPaciente.getAviso_vencimiento().equals("")){
                model.addAttribute("avisoError", "Debe seleccionar una opción");
            }
            model.addAttribute("nombres", usuario.getNombres());
            model.addAttribute("apellidos", usuario.getApellidos());
            model.addAttribute("dni", usuario.getDni());
            model.addAttribute("seguro", usuario.getSeguro().getNombre());
            model.addAttribute("listausuarios", usuarioRepository.findAll());
            model.addAttribute("listaDistritos", distritoRepository.findAll());
            return "paciente/formcompradely";
        }
        else{
            String nombre = usuario.getNombres();
            String apellido = usuario.getApellidos();
            int dni = usuario.getDni();
            String seguro = usuario.getSeguro().getNombre();
            int telefono = pedidosPaciente.getTelefono();
            String medico = pedidosPaciente.getMedico_que_atiende();
            String vencimiento = pedidosPaciente.getAviso_vencimiento();

            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechasoli = fechaActual.format(formatter);

            String direccion = pedidosPaciente.getDireccion();
            String distrito = pedidosPaciente.getDistrito();
            String horaentrega = pedidosPaciente.getHora_de_entrega();
            String estadopedido = "Pendiente";
            int usuid = usuario.getId();
            List<Integer> listidpedidodely = carritoRepository.idpedidoPorUsuIdDely(usuid);
            int idpedido = listidpedidodely.get(0);
            List<String> listidNumtrack = carritoRepository.idNumTrackPorUsuIdDely(usuid);
            String numTrack = listidNumtrack.get(0);

            carritoRepository.registrarMedicamentosPedidoDely(idpedido, usuid);
            carritoRepository.finalizarPedido1(nombre,apellido,dni,telefono,seguro,medico,vencimiento,fechasoli,direccion,distrito,horaentrega,estadopedido,usuid);
            carritoRepository.borrarCarritoPorId(usuid);

            model.addAttribute("numTracking", numTrack);

            return "paciente/finalmsgCompra";
        }
    }

    @PostMapping("/paciente/guardarRecojo")
    public String guardarPedidoReco(@ModelAttribute("pedidosPacienteRecojo") @Valid PedidosPacienteRecojo pedidosPacienteRecojo, BindingResult bindingResult,
                                    Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        if (bindingResult.hasErrors() || pedidosPacienteRecojo.getSede_de_recojo().equals("") || pedidosPacienteRecojo.getMedico_que_atiende().equals("") || pedidosPacienteRecojo.getAviso_vencimiento().equals("")){
            if (pedidosPacienteRecojo.getSede_de_recojo().equals("")){
                model.addAttribute("sedeError", "Debe seleccionar una sede de recojo");
            }
            if (pedidosPacienteRecojo.getMedico_que_atiende().equals("")){
                model.addAttribute("medicoError", "Debe seleccionar una opción");
            }
            if (pedidosPacienteRecojo.getAviso_vencimiento().equals("")){
                model.addAttribute("avisoError", "Debe seleccionar una opción");
            }
            model.addAttribute("nombres", usuario.getNombres());
            model.addAttribute("apellidos", usuario.getApellidos());
            model.addAttribute("dni", usuario.getDni());
            model.addAttribute("seguro", usuario.getSeguro().getNombre());
            model.addAttribute("listausuarios", usuarioRepository.findAll());
            model.addAttribute("listasedes", sedeRepository.findAll());
            return "paciente/formcompra";
        }
        else{
            String nombre = usuario.getNombres();
            String apellido = usuario.getApellidos();
            int dni = usuario.getDni();
            String seguro = usuario.getSeguro().getNombre();
            int telefono = pedidosPacienteRecojo.getTelefono();
            String medico = pedidosPacienteRecojo.getMedico_que_atiende();
            String vencimiento = pedidosPacienteRecojo.getAviso_vencimiento();

            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechasoli = fechaActual.format(formatter);

            String sederecojo = pedidosPacienteRecojo.getSede_de_recojo();
            String estadopedido = "Pendiente";
            int usuid = usuario.getId();
            List<Integer> listidpedidoreco = carritoRepository.idpedidoPorUsuIdReco(usuid);
            int idpedido = listidpedidoreco.get(0);
            List<String> listidNumtrack = carritoRepository.idnumTrackPorUsuIdReco(usuid);
            String numTrack = listidNumtrack.get(0);

            carritoRepository.registrarMedicamentosPedidoReco(idpedido, usuid);
            carritoRepository.finalizarPedido2(nombre,apellido,dni,telefono,seguro,medico,vencimiento,fechasoli,estadopedido,sederecojo,usuid);
            carritoRepository.borrarCarritoPorId(usuid);

            model.addAttribute("numTracking", numTrack);
            return "paciente/finalmsgCompra";
        }
    }

    @GetMapping("paciente/cancelarRegistroPedidoDely")
    public String cancelarRegistroDePedidoDely(Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        carritoRepository.cancelarPedidoDely(usuid);
        return "redirect:/paciente/medicamentos";
    }

    @GetMapping("paciente/cancelarRegistroPedidoReco")
    public String cancelarRegistroDePedidoReco(Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        carritoRepository.cancelarPedidoReco(usuid);
        return "redirect:/paciente/medicamentos";
    }
    /*---------------------------------------*/



    /*QRUD y vista de MIS PEDIDOS*/
    @GetMapping("/paciente/mispedidos")
    public String listaPedidos(Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
            carritoRepository.cancelarPedidoDely(usuid);
        }

        List<PedidosPaciente> listaPedidosDely = pedidosPacienteRepository.pedidosDelivery(usuid);

        model.addAttribute("listaPedidosDely", listaPedidosDely);
        model.addAttribute("tamanolistaPedidosDely", listaPedidosDely.size());
        int llenodely = 1;
        int sinResultadosDely = 0;
        if(listaPedidosDely.isEmpty()){
            llenodely = 0;
        }
        model.addAttribute("llenodely", llenodely);
        model.addAttribute("listaPedidosReco", pedidosPacienteRecojoRepository.findByUsuario(usuario));
        model.addAttribute("tamanolistaPedidosReco", pedidosPacienteRecojoRepository.findByUsuario(usuario).size());
        List <String> tamanolistareco = pedidosPacienteRepository.pedidosRecojo(usuid);
        int llenoreco = 1;
        int sinResultadosReco = 0;
        if(tamanolistareco.isEmpty()){
            llenoreco = 0;
        }
        model.addAttribute("llenoreco", llenoreco);
        model.addAttribute("sinResultadosDely", sinResultadosDely);
        model.addAttribute("sinResultadosReco", sinResultadosReco);

        return "paciente/mispedidos";
    }
    @PostMapping("/paciente/mispedidos/buscadorDely")
    public String buscarPedidoDely(@RequestParam("searchFieldDely") String searchFieldDely,
                                   Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
            carritoRepository.cancelarPedidoDely(usuid);
        }

        List<PedidosPaciente> listaPedidosDely = pedidosPacienteRepository.buscarPedidosDelivery(usuid, searchFieldDely);

        model.addAttribute("listaPedidosDely", listaPedidosDely);
        model.addAttribute("tamanolistaPedidosDely", listaPedidosDely.size());
        int llenodely = 1;
        int sinResultadosDely = 0;
        if(listaPedidosDely.isEmpty()){
            sinResultadosDely = 1;
        }
        model.addAttribute("llenodely", llenodely);
        model.addAttribute("listaPedidosReco", pedidosPacienteRecojoRepository.findByUsuario(usuario));
        model.addAttribute("tamanolistaPedidosReco", pedidosPacienteRecojoRepository.findByUsuario(usuario).size());
        List <String> tamanolistareco = pedidosPacienteRepository.pedidosRecojo(usuid);
        int llenoreco = 1;
        int sinResultadosReco = 0;
        if(tamanolistareco.isEmpty()){
            llenoreco = 0;
        }
        model.addAttribute("llenoreco", llenoreco);
        model.addAttribute("sinResultadosDely", sinResultadosDely);
        model.addAttribute("sinResultadosReco", sinResultadosReco);
        model.addAttribute("listacompletaDely", 1);
        return "paciente/mispedidos";
    }
    @PostMapping("/paciente/mispedidos/buscadorReco")
    public String buscarPedidoReco(@RequestParam("searchFieldReco") String searchFieldReco,
                                   Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
        if(idpedido != null){
            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
            carritoRepository.cancelarPedidoDely(usuid);
        }

        List<PedidosPaciente> listaPedidosDely = pedidosPacienteRepository.pedidosDelivery(usuid);

        model.addAttribute("listaPedidosDely", listaPedidosDely);
        model.addAttribute("tamanolistaPedidosDely", listaPedidosDely.size());
        int llenodely = 1;
        int sinResultadosDely = 0;
        if(listaPedidosDely.isEmpty()){
            llenodely = 0;
        }

        List<PedidosPacienteRecojo> listaPedidosReco = pedidosPacienteRecojoRepository.buscarPedidosReco(usuid, searchFieldReco);

        model.addAttribute("llenodely", llenodely);
        model.addAttribute("listaPedidosReco", listaPedidosReco);
        model.addAttribute("tamanolistaPedidosReco", listaPedidosReco.size());
        int llenoreco = 1;
        int sinResultadosReco = 0;
        if(listaPedidosReco.isEmpty()){
            sinResultadosReco = 1;
        }
        model.addAttribute("llenoreco", llenoreco);
        model.addAttribute("sinResultadosDely", sinResultadosDely);
        model.addAttribute("sinResultadosReco", sinResultadosReco);
        model.addAttribute("listacompletaReco", 1);
        return "paciente/mispedidos";
    }
    /*---------------------------------------*/



    /*QRUD y vista de ESTADO DEL PEDIDO*/
    @GetMapping("/paciente/mispedidos/estadopedidoDely")
    public String estadoTrackDely(Model model, @RequestParam("id") int id){
        Optional<PedidosPaciente> optionalPedidosPaciente = pedidosPacienteRepository.findById(id);

        if(optionalPedidosPaciente.isPresent()){
            PedidosPaciente pedidosPaciente = optionalPedidosPaciente.get();
            List<MedicamentosDelPedido> listaMedicamentosDely = medicamentosDelPedidoRepository.listaMedicamentosDely(id);
            model.addAttribute("listamedicamentodely", listaMedicamentosDely);
            model.addAttribute("pedido", pedidosPaciente);
            return "paciente/estadotrckdely";
        }
        else{
            return "redirect:/paciente/mispedidos";
        }
    }
    @GetMapping("/paciente/mispedidos/estadopedidoReco")
    public String estadoTrackReco(Model model, @RequestParam("id") int id){
        Optional<PedidosPacienteRecojo> optionalPedidosPacienteRecojo = pedidosPacienteRecojoRepository.findById(id);

        if(optionalPedidosPacienteRecojo.isPresent()){
            PedidosPacienteRecojo pedidosPacienteRecojo = optionalPedidosPacienteRecojo.get();
            List<MedicamentoRecojo> listaMedicamentosReco = medicamentosRecojoRepository.listaMedicamentosReco(id);
            model.addAttribute("listamedicamento", listaMedicamentosReco);
            model.addAttribute("pedido", pedidosPacienteRecojo);
            return "paciente/estadotrckreco";
        }
        else{
            return "redirect:/paciente/mispedidos";
        }
    }
    @GetMapping("/paciente/mispedidos/estadopedidoPreorden")
    public String estadoTrackPreorden(Model model, @RequestParam("id") int id){
        Optional<PedidosPaciente> optionalPedidosPaciente = pedidosPacienteRepository.findById(id);

        if(optionalPedidosPaciente.isPresent()){
            PedidosPaciente pedidosPaciente = optionalPedidosPaciente.get();
            List<MedicamentosDelPedido> listaMedicamentosDely = medicamentosDelPedidoRepository.listaMedicamentosDely(id);
            model.addAttribute("listamedicamentopreorden", listaMedicamentosDely);
            model.addAttribute("pedido", pedidosPaciente);
            return "paciente/estadotrckpreorden";
        }
        else{
            return "redirect:/paciente/inicio";
        }
    }
    /*---------------------------------------*/


}
