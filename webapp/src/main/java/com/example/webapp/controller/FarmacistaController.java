package com.example.webapp.controller;
import com.example.webapp.entity.*;
import com.example.webapp.repository.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/farmacista")
public class FarmacistaController {
    private static final int USUARIO_ID_USUARIO = 11;
    @Autowired
    private PedidosPacienteRepository1 pedidoPacienteRepository1;
    @Autowired
    private PedidosPacienteRepository pedidoRepository;
    @GetMapping("/nuevopedido")
    public String Pedidos(Model model) {

        model.addAttribute("pedido", new PedidosPaciente());
        return "farmacista/nuevo_pedido";
    }

    @PostMapping("/pedidoconreceta/guardar")
    public String GuardarPedidoConReceta(Model model,
                                         @RequestParam(name = "hora_de_entrega")
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaDeEntrega,
                                         PedidosPaciente obj, RedirectAttributes attributes) {
        try {

            // Crear un objeto Usuario y establecer su ID
            Usuario usuario = new Usuario();
            usuario.setId(USUARIO_ID_USUARIO);

            // Establecer el usuario asociado con el pedido
            obj.setUsuario(usuario);
            obj.setTipo_de_pedido("CON RECETA");
            obj.setHora_de_entrega(horaDeEntrega.toString()); // ver esta parte
            obj = pedidoPacienteRepository1.save(obj);

            if (obj.getId() > 0) {
                attributes.addFlashAttribute("success", "Pedido con receta registrado!");
                return "redirect:/farmacista/nuevopedido";
            }

            model.addAttribute("error", "No se pudo guardar pedido");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        model.addAttribute("pedido", obj);
        return "farmacista/nuevo_pedido";
    }

    @PostMapping("/pedidosinreceta/guardar")
    public String GuardarPedidoSinReceta(Model model,
                                         PedidosPaciente obj, RedirectAttributes attributes) {
        try {
            // Crear un objeto Usuario y establecer su ID
            Usuario usuario = new Usuario();
            usuario.setId(USUARIO_ID_USUARIO);

            // Establecer el usuario asociado con el pedido
            obj.setUsuario(usuario);
            obj.setTipo_de_pedido("SIN RECETA");
            obj = pedidoPacienteRepository1.save(obj);

            if (obj.getId() > 0) {
                attributes.addFlashAttribute("success", "Pedido sin receta registrado!");
                return "redirect:/farmacista/nuevopedido";
            }

            model.addAttribute("error", "No se pudo guardar pedido");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        model.addAttribute("pedido", obj);
        return "farmacista/nuevo_pedido";
    }
    //Listar pedidos hechos/venta
    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        List<PedidosPaciente> listaPedidos = pedidoRepository.findAll();
        model.addAttribute("listaPedidos", listaPedidos);
        return "farmacista/lista_pedido"; // Reemplaza "nombre_de_tu_plantilla" con el nombre de tu plantilla HTML/Thymeleaf
    }
}
