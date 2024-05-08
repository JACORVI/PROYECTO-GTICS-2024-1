package com.example.webapp.controller.farmacista;

import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Controller

@RequestMapping("/farmacista")
public class FarmacistaController {
    @GetMapping("/farmacista/nuevo_pedido")
    public String Pedidos(Model model){

        return "farmacista/nuevo_pedido";
    }
    @GetMapping("/farmacista/perfil")
    public String Perfil(Model model){

        return "farmacista/perfil";
    }
}
