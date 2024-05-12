package com.example.webapp.controller.adminsede;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.PedidosReposicion;
import com.example.webapp.entity.Usuario;
import com.example.webapp.entity.UsuarioHasSede;
import com.example.webapp.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminSedeController {
    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;
    PedidosReposicionRepository pedidosReposicionRepository;
    UsuarioHasSedeRepository usuarioHasSedeRepository;
    SedeRepository sedeRepository;
    SedeHasMedicamentosRepository sedeHasMedicamentosRepository;

    public AdminSedeController(MedicamentosRepository medicamentosRepository,
                               UsuarioRepository usuarioRepository,
                               PedidosReposicionRepository pedidosReposicionRepository,
                               UsuarioHasSedeRepository usuarioHasSedeRepository,
                               SedeRepository sedeRepository,
                               SedeHasMedicamentosRepository sedeHasMedicamentosRepository) {

        this.medicamentosRepository = medicamentosRepository;

        this.usuarioRepository = usuarioRepository;

        this.pedidosReposicionRepository = pedidosReposicionRepository;

        this.usuarioHasSedeRepository = usuarioHasSedeRepository;

        this.sedeRepository = sedeRepository;

        this.sedeHasMedicamentosRepository = sedeHasMedicamentosRepository;
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
    public String generarPedidosReposicion(Model model){
        model.addAttribute("listaMedConPocoInvent",medicamentosRepository.listarMedicamentosConPocoInvporSede(1));
        return "admin/nuevo_pedido";
    }
    /*---------------------------------------*/

}
