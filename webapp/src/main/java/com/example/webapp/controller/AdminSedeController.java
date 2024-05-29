package com.example.webapp.controller;

import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class AdminSedeController {
    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;
    PedidosReposicionRepository pedidosReposicionRepository;
    UsuarioHasSedeRepository usuarioHasSedeRepository;
    SedeRepository sedeRepository;
    SedeHasMedicamentosRepository sedeHasMedicamentosRepository;
    CarritoRepository carritoRepository;

    public AdminSedeController(MedicamentosRepository medicamentosRepository,
                               UsuarioRepository usuarioRepository,
                               PedidosReposicionRepository pedidosReposicionRepository,
                               UsuarioHasSedeRepository usuarioHasSedeRepository,
                               SedeRepository sedeRepository,
                               SedeHasMedicamentosRepository sedeHasMedicamentosRepository,
                               CarritoRepository carritoRepository) {

        this.medicamentosRepository = medicamentosRepository;

        this.usuarioRepository = usuarioRepository;

        this.pedidosReposicionRepository = pedidosReposicionRepository;

        this.usuarioHasSedeRepository = usuarioHasSedeRepository;

        this.sedeRepository = sedeRepository;

        this.sedeHasMedicamentosRepository = sedeHasMedicamentosRepository;

        this.carritoRepository = carritoRepository;
    }


    /*Vista de inicio (dashboard)*/
    @GetMapping(value = "/admin/paginainicio")
    public String adminsedeinicio(Model model){
        model.addAttribute("listaMedConPocoInvent",medicamentosRepository.medicamentosConPocoInventario(1));
        model.addAttribute("listaMedSoli7",medicamentosRepository.medicamentosSolicitadosxdias(7));
        model.addAttribute("listaMedSoli15",medicamentosRepository.medicamentosSolicitadosxdias(15));
        model.addAttribute("listaMedSoli3",medicamentosRepository.medicamentosSolicitados3meses());
        model.addAttribute("listaSedes",sedeRepository.SedesConMayorCantTransacciones());
        return "admin/paginainicio";
    }
    /*---------------------------------------*/

    /*Vista de lista de medicamentos*/
    @GetMapping(value="/admin/medicamentos")
    public String adminsedeMedicamentos(Model model){
        List<Medicamentos> medxSedeList = medicamentosRepository.listarMedicamentosporSede(1);
        model.addAttribute("listMed",medxSedeList);
        return "admin/medicamentos";
    }
    /*---------------------------------------*/

    /*Vista de lista de doctores*/
    @GetMapping(value="/admin/doctores")
    public String adminsedeDoctores(Model model){
        List<Usuario> doctorxsedeList = usuarioRepository.buscarDoctorporSede(1);
        model.addAttribute("listaDoctor",doctorxsedeList);
        return "admin/doctores";
    }
    /*---------------------------------------*/

    /*Vista de solicitud de farmacistas*/
    @GetMapping(value="/admin/estado_solicitud_farmacistas")
    public String listaSolFarmacistas(Model model){
        List<Usuario> farmacistaxsedeList = usuarioRepository.buscarFarmacistaporSede(1);
        model.addAttribute("listaFarmacista",farmacistaxsedeList);

        return "admin/estado_solicitud_farmacistas";
    }
    /*---------------------------------------*/

    /*Vista de lista de farmacistas*/
    @GetMapping(value="/admin/farmacistas")
    public String adminsedeFarmacistas(Model model){
        List<Usuario> farmacistaxsedeList = usuarioRepository.buscarFarmacistaporSede(1);
        model.addAttribute("listaFarmacista",farmacistaxsedeList);
        return "admin/farmacistas";
    }
    /*---------------------------------------*/

    /*Vista para crear nuevo farmacista*/
    @GetMapping(value="/admin/registrar_farmacista")
    public String adminsedeFarmacistas(RedirectAttributes attr, @ModelAttribute("usuario") Usuario usuario){

        List<Usuario> farmacistaxsedeList = usuarioRepository.buscarFarmacistaporSede(1);
        if(farmacistaxsedeList.size()>2){
            attr.addFlashAttribute("msg1", "Solo se puede registrar un máximo de 3 farmacistas");
            return "redirect:/admin/farmacistas";
        }
        else {
            return "admin/nuevo_farmacista";
        }
    }
    /*---------------------------------------*/

    /*Guarda registro de farmacista*/
    @PostMapping("/admin/guardar_farmacista")
    public String guardarFarmacista(RedirectAttributes attr,
                                    @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //si no hay errores, se realiza el flujo normal
            return "admin/nuevo_farmacista";
        }
        else{
            if (usuario.getId() == 0) {
                attr.addFlashAttribute("msg", "Farmacista creado exitosamente");
                usuarioRepository.save(usuario);
                usuarioHasSedeRepository.AsignarSede(usuario.getId(), 1);
            } else {
                usuarioRepository.save(usuario);
                attr.addFlashAttribute("msg", "Farmacista actualizado exitosamente");
            }


            return "redirect:/admin/farmacistas";
        }

    }
    /*---------------------------------------*/

    /*Edita registro de farmacista*/
    @GetMapping("/admin/editar_farmacista")
    public String editarFarmacista(@ModelAttribute("usuario") Usuario usuario,
                                   Model model, @RequestParam("id") int id) {

        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()) {
            usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "admin/nuevo_farmacista";
        } else {
            return "redirect:/admin/farmacistas";
        }
    }
    /*---------------------------------------*/

    /*Elimina registro de farmacista*/
    @GetMapping("/admin/eliminar_farmacista")
    public String borrarFarmacista(@RequestParam("id") int id,
                                   RedirectAttributes attr) {

        Optional<Usuario> optProduct = usuarioRepository.findById(id);


        if (optProduct.isPresent()) {
            usuarioHasSedeRepository.AsignarSedeBorrando(id);
            usuarioRepository.deleteById(id);
            attr.addFlashAttribute("msg", "Farmacista borrado exitosamente");
        }
        return "redirect:/admin/farmacistas";

    }
    /*---------------------------------------*/

    /*Vista de lista de pedidos de reposición*/
    @GetMapping(value ="/admin/pedidos_reposicion" )
    public String listaPedidosReposicion(Model model){
        List<PedidosReposicion> pedRepoxSedeList = pedidosReposicionRepository.listarPedRepPorIdUsuario(28);
        model.addAttribute("listaPedRep",pedRepoxSedeList);
        return "admin/pedidos_reposicion";
    }
    /*---------------------------------------*/

    /*Vista de lista completa de pedidos de reposición*/
    @GetMapping(value ="/admin/listaPedidosReposicion" )
    public String listaComPedidosReposicion(Model model){
        List<PedidosReposicion> pedRepoxSedeList = pedidosReposicionRepository.listarPedRepPorIdUsuario(28);
        model.addAttribute("listaPedRep",pedRepoxSedeList);
        return "admin/pedidos_reposicion_2";
    }
    /*---------------------------------------*/

    /*Vista de generar nuevo pedido de reposición*/
    @GetMapping(value="/admin/nuevo_pedido")
    public String generarPedidosReposicion(RedirectAttributes attr,Model model) {
        LocalDate fecha = LocalDate.now();
        String fechaString = fecha.toString();


        List<PedidosReposicion> lista = pedidosReposicionRepository.PedRexfecha(fechaString, 28);

        if (lista.isEmpty()) {
            model.addAttribute("listaMedConPocoInvent", medicamentosRepository.listarMedicamentosConPocoInvporSede(1));
            List<Carrito> tamanocarrito = carritoRepository.listarCarritoxUsuario(28);
            if (tamanocarrito.isEmpty()) {
                int tamCarrito = 0;
                model.addAttribute("tamanhoCarrito", tamCarrito);
            } else {
                model.addAttribute("tamanhoCarrito", tamanocarrito.size());
            }

            //generador de numero de pedidos
            List<String> estadosdecompraporId = carritoRepository.estadosDeCompraPorUsuarioId(28);
            boolean soloEstadosRegistrados = true;

            String banco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            String numpedido = "";
            for (String palabra : estadosdecompraporId) {
                if (palabra != null && palabra.equals("Comprando")) {
                    soloEstadosRegistrados = false;
                    break;
                }
            }
            if (estadosdecompraporId.isEmpty() || soloEstadosRegistrados) {
                for (int x = 0; x < 12; x++) {
                    if (x > 0 && x % 4 == 0) {
                        String guion = "-";
                        numpedido += guion;
                    }
                    int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
                    char caracterAleatorio = banco.charAt(indiceAleatorio);
                    numpedido += caracterAleatorio;
                }
            }
            model.addAttribute("numPedido", numpedido);
            return "admin/nuevo_pedido";
        }
        else{
            attr.addFlashAttribute("msg1", "Solo se puede hacer un pedido de reposición por día");
            return "redirect:/admin/pedidos_reposicion";
        }
    }
    /*---------------------------------------*/

    /*Añadiendo el primer medicamento al carrito*/
    @GetMapping("/admin/añadirCarrito1")
    public String anadirMedicamentoAlCarrito1(@RequestParam("id") int id, @RequestParam("numpedido") String numpedido, RedirectAttributes attr){
        int usuid = 28;

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
            int cantidad = 50;
            carritoRepository.AnadirAlCarrito(id, usuid, cantidad, numpedido, estadocompra);
            attr.addFlashAttribute("msg","Se agrego un nuevo medicamento!");
        }
        return "redirect:/admin/nuevo_pedido";
    }
    /*---------------------------------------*/

    /*Añadiendo más medicamentos al carrito*/
    @GetMapping("/admin/añadirCarrito2")
    public String anadirMedicamentoAlCarrito2(@RequestParam("id") int id, RedirectAttributes attr){
        int usuid = 28;
        String estadocompra = "Comprando";
        List<Carrito> duplicados = carritoRepository.buscarDuplicados(id);
        if (duplicados.isEmpty()){
            int cantidad = 50;
            List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(usuid);
            String numpedido = numeropedidoporId.get(0);
            carritoRepository.AnadirAlCarrito(id, usuid, cantidad, numpedido, estadocompra);
            attr.addFlashAttribute("msg","Se agrego un nuevo medicamento!");
        }
        else{
            attr.addFlashAttribute("msg1","Ya se encuentra agregado el medicamento seleccionado!");
        }
        return "redirect:/admin/nuevo_pedido";
    }
    /*---------------------------------------*/

    /*Numeros aleatorios*/
    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
    /*---------------------------------------*/

    /*Vista de medicamentos agregados al carrito*/
    @GetMapping("/admin/carrito")
    public String listarProductosCarrito(RedirectAttributes attr,Model model) {
        List<Carrito> tamanocarrito = carritoRepository.listarCarritoxUsuario(28);
        if (!tamanocarrito.isEmpty()) {

            List<String> numeropedidoporId = carritoRepository.numPedidoPorUsuarioId(28);
            if (!numeropedidoporId.isEmpty()) {
                String numpedido = numeropedidoporId.get(0);
                model.addAttribute("numpedido", numpedido);
            }
            List<Carrito> listadodelcarritorxNumPed = carritoRepository.listadodelcarritorxNumPed(numeropedidoporId.get(0));
            int car = 0;
            if (listadodelcarritorxNumPed.isEmpty()) {
                String msg1 = "Su carrito esta vacio.";
                String msg2 = "No agrego ningun producto a su carrito.";
                car = 1;
                model.addAttribute("msg1", msg1);
                model.addAttribute("msg2", msg2);
                model.addAttribute("car", car);
            } else {
                model.addAttribute("listadoDelCarrito", listadodelcarritorxNumPed);
                List<Double> listaPrecioxCantidad = carritoRepository.CantidadxPrecioUnitarioxNumPed(numeropedidoporId.get(0));
                double sumaTotal = 0.0;
                for (Double valor : listaPrecioxCantidad) {
                    sumaTotal += valor;
                }
                String sumaTotal2D = String.format("%.2f", sumaTotal);
                model.addAttribute("precioTotal", sumaTotal2D);
                model.addAttribute("car", car);

            }


            return "admin/carrito";
        }
        else {
            return "redirect:/admin/nuevo_pedido";
        }
    }
    /*---------------------------------------*/

    /*Borrar medicamentos del carrito*/
    @GetMapping("/admin/carrito/borrar")
    public String borrarElementoCarrito(@RequestParam("id") int id) {
        int usuid = 28;
        carritoRepository.borrarElementoCarrito(id, usuid);
        return "redirect:/admin/carrito";
    }
    /*---------------------------------------*/

    /*Vista del formulario del pedido*/
    @GetMapping("/admin/carrito/formulario")
    public String registrarPedido(@ModelAttribute("pedidosReposicion") PedidosReposicion pedidosReposicion,@RequestParam("costototal") double costototal,
                                  Model model){

        String estadopedido = "Solicitado";
        LocalDate fecha = LocalDate.now();
        String fechaString = fecha.toString();

        int usuid = 28;

        carritoRepository.registrarPedidoRepo1(usuid,fechaString,costototal,"2024-09-25",estadopedido,1);

        int IdRep = pedidosReposicionRepository.idPedRexfecha(fechaString,usuid);
        pedidosReposicion.setId(IdRep);
        pedidosReposicion.setFecha_solicitud(fechaString);
        pedidosReposicion.setCosto_total(costototal);
        pedidosReposicion.setFecha_entrega("2024-09-25");
        pedidosReposicion.setEstado_de_reposicion(estadopedido);
        model.addAttribute("pedRep",pedidosReposicion );


        return "admin/formCompra";

    }
    /*---------------------------------------*/

    @PostMapping("/admin/guardarOrden")
    public String guardarPedidoReco(RedirectAttributes attr, @ModelAttribute("pedidosReposicion") PedidosReposicion pedidosReposicion,
                                    Model model) {
        List<Carrito> carritoxusId = carritoRepository.listarCarritoxUsuario(28);
        for (Carrito producto : carritoxusId) {
            carritoRepository.registrarPedidoRepo3(pedidosReposicion.getId(), 28,1,producto.getMedicamentos_id_medicamentos().getId(),50);
        }

        attr.addFlashAttribute("msg", "Orden creada exitosamente");

        carritoRepository.borrarCarritoPorId(28);


        return "redirect:/admin/pedidos_reposicion";
    }

    @GetMapping("/admin/editar_orden")
    public String editarOrden(@ModelAttribute("pedidosReposicion") PedidosReposicion pedidosReposicion,
                              Model model, @RequestParam("id") int id) {

        Optional<PedidosReposicion> optUsuario = pedidosReposicionRepository.findById(id);

        if (optUsuario.isPresent()) {
            pedidosReposicion = optUsuario.get();
            model.addAttribute("pedidoRepo", pedidosReposicion);
            return "admin/editarOrden";
        } else {
            return "redirect:/admin/pedidos_reposicion";
        }
    }

    @GetMapping("admin/cancelarRegistro")
    public String cancelarRegistro(@RequestParam("id") int id,RedirectAttributes attr){
        int usuid = 28;
        pedidosReposicionRepository.eliminarTodo(id);
        carritoRepository.eliminarPedidoRepo(usuid);
        carritoRepository.eliminarCarrito(usuid);

        attr.addFlashAttribute("msg", "Orden cancelada exitosamente");
        return "redirect:/admin/pedidos_reposicion";
    }
    /*---------------------------------------*/

    @GetMapping("admin/eliminarOrdenDeRepo")
    public String eliminarOrden(@RequestParam("id") int id,RedirectAttributes attr){
        int usuid = 28;
        pedidosReposicionRepository.eliminarTodo(id);
        carritoRepository.eliminarPedidoRepo(usuid);
        carritoRepository.eliminarCarrito(usuid);

        attr.addFlashAttribute("msg", "Solicitud de orden eliminada exitosamente");
        return "redirect:/admin/pedidos_reposicion";
    }
    /*---------------------------------------*/



}