package com.example.webapp.controller;
import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class FarmacistaController {

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

    public FarmacistaController(MedicamentosRepository medicamentosRepository,
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
    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
    /*---------------------------------------*/

    @GetMapping("/farmacista/medicamentos")
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

        List<Medicamentos> lista = medicamentosRepository.buscarMedicamentoGeneral(0);
        List<String> listafotos = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            byte[] fotoBytes = lista.get(i).getFoto();
            String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
            listafotos.add(fotoBase64);
        }

        model.addAttribute("listaMedicamentos",lista);
        model.addAttribute("listaFotos", listafotos);
        model.addAttribute("cantidadMedicamentos",lista.size());


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

        return "farmacista/medicamentos";
    }


    /*QRUD y vista de MEDICAMENTOS*/

    @PostMapping("/farmacista/medicamentos/buscador")
    public String buscarMedicamentos(@RequestParam("searchField") String searchField,
                                     Authentication authentication, Model model){
        if(searchField.equals("")){
            return "redirect:/farmacista/medicamentos";
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

        List<Medicamentos> lista = medicamentosRepository.buscarMedicamento(searchField);
        List<String> listafotos = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            byte[] fotoBytes = lista.get(i).getFoto();
            String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
            listafotos.add(fotoBase64);
        }

        model.addAttribute("listaMedicamentos", lista);
        model.addAttribute("listaFotos", listafotos);
        model.addAttribute("cantidadMedicamentos", lista.size());

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

        return "farmacista/medicamentos";
    }

    @PostMapping("/farmacista/medicamentos/filtrar")
    public String filtrarMedicamentos(@RequestParam("filtroCategoria") String categoria, @RequestParam("filtroOrden") String orden,
                                      Authentication authentication, Model model){

        if(categoria.equals("") && orden.equals("")){
            return "redirect:/farmacista/medicamentos";
        }
        if(!categoria.equals("")){
            if(!orden.equals("")) {
                if (orden.equals("1")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro1(categoria);
                    List<String> listafotos = new ArrayList<>();

                    for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                        byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                        String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                        listafotos.add(fotoBase64);
                    }

                    model.addAttribute("listaFotos", listafotos);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "mayor a menor precio");
                }
                if (orden.equals("2")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro2(categoria);
                    List<String> listafotos = new ArrayList<>();

                    for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                        byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                        String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                        listafotos.add(fotoBase64);
                    }

                    model.addAttribute("listaFotos", listafotos);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "menor a mayor precio");
                }
                if (orden.equals("3")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro3(categoria);
                    List<String> listafotos = new ArrayList<>();

                    for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                        byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                        String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                        listafotos.add(fotoBase64);
                    }

                    model.addAttribute("listaFotos", listafotos);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "antiguo a nuevo");
                }
                if (orden.equals("4")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro4(categoria);
                    List<String> listafotos = new ArrayList<>();

                    for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                        byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                        String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                        listafotos.add(fotoBase64);
                    }

                    model.addAttribute("listaFotos", listafotos);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "A -> Z");
                }
                if (orden.equals("5")) {
                    List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosCategoriaFiltro5(categoria);
                    List<String> listafotos = new ArrayList<>();

                    for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                        byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                        String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                        listafotos.add(fotoBase64);
                    }

                    model.addAttribute("listaFotos", listafotos);
                    model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                    model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("orden", "Z -> A");
                }
            }
            else{
                List<Medicamentos> medicamentosPorCategoria = medicamentosRepository.listaMedicamentosPorCategoria(categoria);
                List<String> listafotos = new ArrayList<>();

                for (int i = 0; i < medicamentosPorCategoria.size(); i++) {
                    byte[] fotoBytes = medicamentosPorCategoria.get(i).getFoto();
                    String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                    listafotos.add(fotoBase64);
                }

                model.addAttribute("listaFotos", listafotos);
                model.addAttribute("listaMedicamentos", medicamentosPorCategoria);
                model.addAttribute("cantidadMedicamentos",medicamentosPorCategoria.size());
                model.addAttribute("categoria", categoria);
            }
        }
        else{
            if (orden.equals("1")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro1();
                List<String> listafotos = new ArrayList<>();

                for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                    byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                    String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                    listafotos.add(fotoBase64);
                }

                model.addAttribute("listaFotos", listafotos);
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                model.addAttribute("orden", "mayor a menor precio");
            }
            if (orden.equals("2")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro2();
                List<String> listafotos = new ArrayList<>();

                for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                    byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                    String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                    listafotos.add(fotoBase64);
                }

                model.addAttribute("listaFotos", listafotos);
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                model.addAttribute("orden", "menor a mayor precio");
            }
            if (orden.equals("3")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro3();
                List<String> listafotos = new ArrayList<>();

                for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                    byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                    String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                    listafotos.add(fotoBase64);
                }

                model.addAttribute("listaFotos", listafotos);
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                model.addAttribute("orden", "antiguo a nuevo");
            }
            if (orden.equals("4")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro4();
                List<String> listafotos = new ArrayList<>();

                for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                    byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                    String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                    listafotos.add(fotoBase64);
                }

                model.addAttribute("listaFotos", listafotos);
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
                model.addAttribute("orden", "A -> Z");
            }
            if (orden.equals("5")) {
                List<Medicamentos> medicamentosFiltrados = medicamentosRepository.listaMedicamentosFiltro5();
                List<String> listafotos = new ArrayList<>();

                for (int i = 0; i < medicamentosFiltrados.size(); i++) {
                    byte[] fotoBytes = medicamentosFiltrados.get(i).getFoto();
                    String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                    listafotos.add(fotoBase64);
                }

                model.addAttribute("listaFotos", listafotos);
                model.addAttribute("listaMedicamentos", medicamentosFiltrados);
                model.addAttribute("cantidadMedicamentos",medicamentosFiltrados.size());
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

        return "farmacista/medicamentos";
    }
    @GetMapping("/farmacista/medicamentos/info")
    @ResponseBody
    public List<String> informacionDelMedicamento(@RequestParam("id") String strId){
        Integer id = Integer.parseInt(strId);
        Optional<Medicamentos> medicamentos = medicamentosRepository.findById(id);
        List<String> infoMedicamentos = new ArrayList<>();
        if (medicamentos.isPresent()){
            Medicamentos medicamento = medicamentos.get();

            String idStr = Integer.toString(medicamento.getId());
            infoMedicamentos.add(idStr);
            infoMedicamentos.add(medicamento.getNombre());
            infoMedicamentos.add(medicamento.getCategoria());
            infoMedicamentos.add(medicamento.getDescripcion());
            byte[] fotoBytes1 = medicamento.getFoto();
            String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes1);
            infoMedicamentos.add(fotoBase64);
            return infoMedicamentos;
        }
        else{
            return null;
        }
    }




    @GetMapping("/farmacista/añadirCarrito1")
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
        return "redirect:/farmacista/medicamentos";
    }

    @GetMapping("/farmacista/añadirCarrito2")
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
        return "redirect:/farmacista/medicamentos";
    }

    /*QRUD y vista del CARRITO*/
    @ResponseBody
    @GetMapping("/tamañocarritof")
    public Integer listarProductosCarritoRT(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        List<Carrito> carritoPorId = carritoRepository.carritoPorId(usuid);
        return carritoPorId.size();
    }
    @GetMapping("/farmacista/carrito")
    public String listarProductosCarritoRT(Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(usuid);
        if (!numeropedidoporId.isEmpty()){
            String numpedido = numeropedidoporId.get(0);
            model.addAttribute("numpedido", numpedido);
        }
        List<Carrito> listadodelcarritort = carritoRepository.listarCarrito(usuid);
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
            List<Double> listaPrecioxCantidad = carritoRepository.CantidadxPrecioUnitario(usuid);
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
    public String listarProductosCarritoDL(Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(usuid);
        if (!numeropedidoporId.isEmpty()){
            String numpedido = numeropedidoporId.get(0);
            model.addAttribute("numpedido", numpedido);
        }
        List<Carrito> listadodelcarritodl = carritoRepository.listarCarrito(usuid);
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
            List<Double> listaPrecioxCantidad = carritoRepository.CantidadxPrecioUnitario(usuid);
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
        return "farmacista/carrito";
    }

    @GetMapping("/farmacista/carrito/borrar")
    public String borrarElementoCarrito(Model model, Authentication authentication,
                                        @RequestParam("id") int id) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        carritoRepository.borrarElementoCarrito(id, usuid);
        return "redirect:/farmacista/carrito";
    }
/*ver si es necesario*/
    @GetMapping("/farmacista/carrito/registrarPedido")
    public String registrarPedido(Model model, RedirectAttributes redirectAttributes, Authentication authentication,
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
            List<Integer> lista = carritoRepository.listaPedidosPorCancelar1(usuid);
            if(!lista.isEmpty()){
                redirectAttributes.addFlashAttribute("msg1", "Tienes un pago pendiente.");
                return "redirect:/farmacista/mispedidos";
            }
            tipopedido = "Web - Delivery";
            String validacionpedido = "Pendiente";
            String estadopedido = "Registrando";
            carritoRepository.registrarPedidoDely(costototal, tipopedido, validacionpedido, estadopedido, numtrack, usuid);
            return "redirect:/farmacista/carrito/nuevoPedidoDelivery";
        }
        else{
            List<Integer> lista = carritoRepository.listaPedidosPorCancelar3(usuid);
            if(!lista.isEmpty()){
                redirectAttributes.addFlashAttribute("msg2", "Tienes un pago pendiente.");
                return "redirect:/farmacista/mispedidos#pedidosReco";
            }
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
                                              Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        model.addAttribute("nombres", usuario.getNombres());
        model.addAttribute("apellidos", usuario.getApellidos());
        model.addAttribute("dni", usuario.getDni());
        model.addAttribute("seguro", usuario.getSeguro().getNombre());
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        model.addAttribute("listaDistritos", distritoRepository.findAll());
        return "farmacista/formcompradely";
    }

    @GetMapping("/farmacista/carrito/nuevoPedidoRecojo")
    public String formParaFinalizarCompraRecojo(@ModelAttribute("pedidosPacienteRecojo") PedidosPacienteRecojo pedidosPacienteRecojo,
                                                Model model, Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        model.addAttribute("nombres", usuario.getNombres());
        model.addAttribute("apellidos", usuario.getApellidos());
        model.addAttribute("dni", usuario.getDni());
        model.addAttribute("seguro", usuario.getSeguro().getNombre());
        model.addAttribute("listausuarios", usuarioRepository.findAll());
        model.addAttribute("listasedes", sedeRepository.findAll());
        return "farmacista/formcompra";
    }

    @PostMapping("/farmacista/guardarDely")
    public String guardarPedidoDely(@RequestParam("foto1") Part foto1,
                                    @ModelAttribute("pedidosPaciente") @Valid PedidosPaciente pedidosPaciente, BindingResult bindingResult,
                                    Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        boolean telefonoErrors = pedidosPaciente.getTelefono() == null || pedidosPaciente.getTelefono().equals("") || pedidosPaciente.getTelefono()<900000000 || pedidosPaciente.getTelefono()>999999999;
        boolean imagenValida = foto1.getContentType().contains("application/octet-stream") || foto1.getContentType().contains("image/jpeg") || foto1.getContentType().contains("image/png") || foto1.getContentType().contains("image/jpeg"); ;
        boolean evitaAtaquesLFI = foto1.getSubmittedFileName().contains("..");

        if (bindingResult.hasErrors() || pedidosPaciente.getDistrito().equals("") || pedidosPaciente.getMedico_que_atiende().equals("") || pedidosPaciente.getAviso_vencimiento().equals("") || telefonoErrors || !imagenValida || evitaAtaquesLFI){
            if (pedidosPaciente.getTelefono() == null || pedidosPaciente.getTelefono().equals("")){
                model.addAttribute("telefonoError", "El número de celular no puede quedar vacio.");
            }
            else{
                if(pedidosPaciente.getTelefono()<900000000 || pedidosPaciente.getTelefono()>999999999){
                    model.addAttribute("telefonoError", "El número de celular tiene que tener 9 dígitos y empezar con 9.");
                }
            }
            if(!imagenValida || evitaAtaquesLFI){
                model.addAttribute("fotoError", "Solo se aceptan archivos de tipo JPG, JPEG y PNG");
            }
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
            return "farmacista/formcompradely";
        }
        else{
            if(foto1.getContentType().equals("application/octet-stream")){
                int usuid = usuario.getId();

                List<Integer> ids = carritoRepository.idpedidoPorUsuIdDely(usuid);
                Integer idped = ids.get(0);
                Optional<PedidosPaciente> optionalPedidosPaciente = pedidosPacienteRepository.findById(idped);

                if (optionalPedidosPaciente.isPresent()){
                    PedidosPaciente pedidodely = optionalPedidosPaciente.get();

                    pedidosPaciente.setCosto_total(pedidodely.getCosto_total());
                    pedidosPaciente.setTipo_de_pedido(pedidodely.getTipo_de_pedido());
                    pedidosPaciente.setValidacion_del_pedido("Pendiente");
                    pedidosPaciente.setEstado_del_pedido("Por cancelar");

                    String numTrack = pedidodely.getNumero_tracking();
                    pedidosPaciente.setNumero_tracking(pedidodely.getNumero_tracking());

                    pedidosPaciente.setUsuario(usuario);
                    pedidosPaciente.setNombre_paciente(usuario.getNombres());
                    pedidosPaciente.setApellido_paciente(usuario.getApellidos());
                    pedidosPaciente.setDni(usuario.getDni());
                    pedidosPaciente.setSeguro(usuario.getSeguro().getNombre());

                    LocalDate fechaActual = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechasoli = fechaActual.format(formatter);
                    pedidosPaciente.setFecha_solicitud(fechasoli);

                    Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
                    if(idpedido != null){
                        carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
                        carritoRepository.cancelarPedidoDely(usuid);
                    }
                    pedidosPacienteRepository.save(pedidosPaciente);
                    List<Integer> listidpedidodely = carritoRepository.idpedidoPorUsuIdDelyMedicamentos(usuid);
                    int idpedidomed = listidpedidodely.get(0);
                    carritoRepository.registrarMedicamentosPedidoDely(idpedidomed, usuid);
                    carritoRepository.borrarCarritoPorId(usuid);

                    model.addAttribute("numTracking", numTrack);
                    model.addAttribute("delivery", 1);
                }
            }
            else{
                int usuid = usuario.getId();

                List<Integer> ids = carritoRepository.idpedidoPorUsuIdDely(usuid);
                Integer idped = ids.get(0);
                Optional<PedidosPaciente> optionalPedidosPaciente = pedidosPacienteRepository.findById(idped);

                if (optionalPedidosPaciente.isPresent()){
                    PedidosPaciente pedidodely = optionalPedidosPaciente.get();

                    pedidosPaciente.setCosto_total(pedidodely.getCosto_total());
                    pedidosPaciente.setTipo_de_pedido(pedidodely.getTipo_de_pedido());
                    pedidosPaciente.setValidacion_del_pedido("Pendiente");
                    pedidosPaciente.setEstado_del_pedido("Por cancelar");

                    String numTrack = pedidodely.getNumero_tracking();
                    pedidosPaciente.setNumero_tracking(pedidodely.getNumero_tracking());

                    pedidosPaciente.setUsuario(usuario);
                    pedidosPaciente.setNombre_paciente(usuario.getNombres());
                    pedidosPaciente.setApellido_paciente(usuario.getApellidos());
                    pedidosPaciente.setDni(usuario.getDni());
                    pedidosPaciente.setSeguro(usuario.getSeguro().getNombre());

                    LocalDate fechaActual = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechasoli = fechaActual.format(formatter);
                    pedidosPaciente.setFecha_solicitud(fechasoli);

                    try {
                        InputStream fotoStream=foto1.getInputStream();
                        byte[] fotoBytes=fotoStream.readAllBytes();
                        pedidosPaciente.setReceta_foto(fotoBytes);
                        Integer idpedido = carritoRepository.idPedidoRegistrando(usuid);
                        if(idpedido != null){
                            carritoRepository.borrarMedicamentosAlCancelar(usuid, idpedido);
                            carritoRepository.cancelarPedidoDely(usuid);
                        }
                        pedidosPacienteRepository.save(pedidosPaciente);
                        List<Integer> listidpedidodely = carritoRepository.idpedidoPorUsuIdDelyMedicamentos(usuid);
                        int idpedidomed = listidpedidodely.get(0);
                        carritoRepository.registrarMedicamentosPedidoDely(idpedidomed, usuid);
                    } catch (IOException e) {
                        model.addAttribute("nombres", usuario.getNombres());
                        model.addAttribute("apellidos", usuario.getApellidos());
                        model.addAttribute("dni", usuario.getDni());
                        model.addAttribute("seguro", usuario.getSeguro().getNombre());
                        model.addAttribute("listausuarios", usuarioRepository.findAll());
                        model.addAttribute("listaDistritos", distritoRepository.findAll());
                        model.addAttribute("fotoError", "Ocurrió un error al subir la imagen, vuelva a intentarlo");
                        return "farmacista/formcompradely";
                    }

                    carritoRepository.borrarCarritoPorId(usuid);

                    model.addAttribute("numTracking", numTrack);
                    model.addAttribute("delivery", 1);
                }
            }

            return "farmacista/finalmsgCompra";
        }
    }

    @PostMapping("/farmacista/guardarRecojo")
    public String guardarPedidoReco(@RequestParam("foto1") Part foto1,
                                    @ModelAttribute("pedidosPacienteRecojo") PedidosPacienteRecojo pedidosPacienteRecojo,
                                    Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        boolean telefonoErrors = pedidosPacienteRecojo.getTelefono() == null || pedidosPacienteRecojo.getTelefono().equals("") || pedidosPacienteRecojo.getTelefono()<900000000 || pedidosPacienteRecojo.getTelefono()>999999999;
        boolean imagenValida = foto1.getContentType().contains("application/octet-stream") || foto1.getContentType().contains("image/jpeg") || foto1.getContentType().contains("image/png") || foto1.getContentType().contains("image/jpeg");
        boolean evitaAtaquesLFI = foto1.getSubmittedFileName().contains("..");

        if (pedidosPacienteRecojo.getSede_de_recojo().equals("") || pedidosPacienteRecojo.getMedico_que_atiende().equals("") || pedidosPacienteRecojo.getAviso_vencimiento().equals("") || telefonoErrors || !imagenValida || evitaAtaquesLFI){
            if (pedidosPacienteRecojo.getTelefono() == null || pedidosPacienteRecojo.getTelefono().equals("")){
                model.addAttribute("telefonoError", "El número de celular no puede quedar vacio.");
            }
            else{
                if(pedidosPacienteRecojo.getTelefono()<900000000 || pedidosPacienteRecojo.getTelefono()>999999999){
                    model.addAttribute("telefonoError", "El número de celular tiene que tener 9 dígitos y empezar con 9.");
                }
            }
            if(!imagenValida || evitaAtaquesLFI){
                model.addAttribute("fotoError", "Solo se aceptan archivos de tipo JPG, JPEG y PNG");
            }
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
            return "farmacista/formcompra";
        }
        else{
            if(foto1.getContentType().equals("application/octet-stream")){
                int usuid = usuario.getId();

                List<Integer> ids = carritoRepository.idpedidoPorUsuIdReco(usuid);
                Integer idped = ids.get(0);
                Optional<PedidosPacienteRecojo> optionalPedidosPaciente = pedidosPacienteRecojoRepository.findById(idped);

                if (optionalPedidosPaciente.isPresent()){
                    PedidosPacienteRecojo pedidoreco = optionalPedidosPaciente.get();

                    pedidosPacienteRecojo.setCosto_total(pedidoreco.getCosto_total());
                    pedidosPacienteRecojo.setTipo_de_pedido(pedidoreco.getTipo_de_pedido());
                    pedidosPacienteRecojo.setValidacion_del_pedido("Pendiente");
                    pedidosPacienteRecojo.setEstado_del_pedido("Por cancelar");

                    String numTrack = pedidoreco.getNumero_tracking();
                    pedidosPacienteRecojo.setNumero_tracking(pedidoreco.getNumero_tracking());

                    pedidosPacienteRecojo.setUsuario(usuario);
                    pedidosPacienteRecojo.setNombre_paciente(usuario.getNombres());
                    pedidosPacienteRecojo.setApellido_paciente(usuario.getApellidos());
                    pedidosPacienteRecojo.setDni(usuario.getDni());
                    pedidosPacienteRecojo.setSeguro(usuario.getSeguro().getNombre());

                    LocalDate fechaActual = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechasoli = fechaActual.format(formatter);
                    pedidosPacienteRecojo.setFecha_solicitud(fechasoli);

                    carritoRepository.cancelarPedidoReco(usuid);
                    pedidosPacienteRecojoRepository.save(pedidosPacienteRecojo);
                    List<Integer> listidpedidoreco = carritoRepository.idpedidoPorUsuIdRecoMedicamentos(usuid);
                    int idpedidomed = listidpedidoreco.get(0);
                    carritoRepository.registrarMedicamentosPedidoReco(idpedidomed, usuid);
                    carritoRepository.borrarCarritoPorId(usuid);

                    model.addAttribute("numTracking", numTrack);
                    model.addAttribute("recojo", 1);
                }
            }
            else{
                int usuid = usuario.getId();

                List<Integer> ids = carritoRepository.idpedidoPorUsuIdReco(usuid);
                Integer idped = ids.get(0);
                Optional<PedidosPacienteRecojo> optionalPedidosPaciente = pedidosPacienteRecojoRepository.findById(idped);

                if (optionalPedidosPaciente.isPresent()){
                    PedidosPacienteRecojo pedidoreco = optionalPedidosPaciente.get();

                    pedidosPacienteRecojo.setCosto_total(pedidoreco.getCosto_total());
                    pedidosPacienteRecojo.setTipo_de_pedido(pedidoreco.getTipo_de_pedido());
                    pedidosPacienteRecojo.setValidacion_del_pedido("Pendiente");
                    pedidosPacienteRecojo.setEstado_del_pedido("Por cancelar");

                    String numTrack = pedidoreco.getNumero_tracking();
                    pedidosPacienteRecojo.setNumero_tracking(pedidoreco.getNumero_tracking());

                    pedidosPacienteRecojo.setUsuario(usuario);
                    pedidosPacienteRecojo.setNombre_paciente(usuario.getNombres());
                    pedidosPacienteRecojo.setApellido_paciente(usuario.getApellidos());
                    pedidosPacienteRecojo.setDni(usuario.getDni());
                    pedidosPacienteRecojo.setSeguro(usuario.getSeguro().getNombre());

                    LocalDate fechaActual = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechasoli = fechaActual.format(formatter);
                    pedidosPacienteRecojo.setFecha_solicitud(fechasoli);

                    try {
                        InputStream fotoStream=foto1.getInputStream();
                        byte[] fotoBytes=fotoStream.readAllBytes();
                        pedidosPacienteRecojo.setReceta_foto(fotoBytes);
                        carritoRepository.cancelarPedidoReco(usuid);
                        pedidosPacienteRecojoRepository.save(pedidosPacienteRecojo);
                        List<Integer> listidpedidoreco = carritoRepository.idpedidoPorUsuIdRecoMedicamentos(usuid);
                        int idpedidomed = listidpedidoreco.get(0);
                        carritoRepository.registrarMedicamentosPedidoReco(idpedidomed, usuid);
                    } catch (IOException e) {
                        model.addAttribute("nombres", usuario.getNombres());
                        model.addAttribute("apellidos", usuario.getApellidos());
                        model.addAttribute("dni", usuario.getDni());
                        model.addAttribute("seguro", usuario.getSeguro().getNombre());
                        model.addAttribute("listausuarios", usuarioRepository.findAll());
                        model.addAttribute("listasedes", sedeRepository.findAll());
                        model.addAttribute("fotoError", "Ocurrió un error al subir la imagen, vuelva a intentarlo");
                        return "farmacista/formcompradely";
                    }

                    carritoRepository.borrarCarritoPorId(usuid);

                    model.addAttribute("numTracking", numTrack);
                    model.addAttribute("recojo", 1);
                }
            }

            return "farmacista/finalmsgCompra";
        }
    }

    @GetMapping("farmacista/cancelarRegistroPedidoDely")
    public String cancelarRegistroDePedidoDely(Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        carritoRepository.cancelarPedidoDely(usuid);
        return "redirect:/farmacista/medicamentos";
    }

    @GetMapping("farmacista/cancelarRegistroPedidoReco")
    public String cancelarRegistroDePedidoReco(Authentication authentication){
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        int usuid = usuario.getId();
        carritoRepository.cancelarPedidoReco(usuid);
        return "redirect:/farmacista/medicamentos";
    }
    /*---------------------------------------*/
    @GetMapping("/farmacista/mensaje")
    public String mostrarMensajeria(Model model) {
        // Agrega aquí cualquier atributo necesario al modelo
        return "farmacista/mensajeriaf";
    }

}
