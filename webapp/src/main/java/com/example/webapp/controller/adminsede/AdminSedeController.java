package com.example.webapp.controller.adminsede;

import com.example.webapp.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        /*List<Medicamentos> listMed = medicamentosRepository.findAll();
        model.addAttribute("listMed",listMed );*/
        return "admin/medicamentosSupAdmin";
    }

    @GetMapping(value="/admins/nuevo_pedido")
    public String adminsedePedidos(){

        return "admin/nuevo_pedido";
    }

    @GetMapping(value="/admin/farmacistas")
    public String adminsedeFarmacistas(){

        return "admin/farmacistas";
    }

    @GetMapping(value="/admin/estado_soliciutd_farmacistas")
    public String adminsedeSolFarmacistas(){

        return "admin/estado_soliciutd_farmacistas";
    }
}
