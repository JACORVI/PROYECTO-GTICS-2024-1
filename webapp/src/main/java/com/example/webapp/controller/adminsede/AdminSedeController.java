package com.example.webapp.controller.adminsede;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Usuario;
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


    @GetMapping(value = "/admin/inicioSupAdmin")
    public String adminsedeinicio(){
        return "admin/inicioSupAdmin";
    }
    @GetMapping(value ="/admin/pedidos_reposicion" )
    public String adminsedereposicion(){
        return "admin/pedidos_reposicion";
    }
    @GetMapping(value="/admin/medicamentosSupAdmin")
    public String adminsedemedciamentos(Model model){
        List<Medicamentos> listMed = medicamentosRepository.findAll();
        model.addAttribute("listMed",listMed );
        return "admin/medicamentosSupAdmin";
    }

    @GetMapping(value="/admins/nuevo_pedido")
    public String adminsedePedidos(){

        return "admin/nuevo_pedido";
    }

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
    public String adminsedeFarmacistas(@ModelAttribute("usuario") Usuario usuario){

        return "admin/nuevo_farmacista";
    }
    /*---------------------------------------*/

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

    @GetMapping(value="/admin/estado_soliciutd_farmacistas")
    public String adminsedeSolFarmacistas(){

        return "admin/estado_soliciutd_farmacistas";
    }
}
