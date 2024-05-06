package com.example.webapp.controller.adminsede;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminSedeController {
    @GetMapping(value = "/adminsede/inicio")
    public String adminsedeinicio(){
        return "admin/Vista_Principal_Admin";
    }
    @GetMapping(value ="/adminsede/reposición" )
    public String adminsedereposicion(){
        return "admin/Vista_Pedidos_de_Reposición";
    }
    @GetMapping(value="/adminsede/medicamentos")
    public String adminsedemedciamentos(){
        return "admin/Vista_Medicamentos_Admin";
    }
}
