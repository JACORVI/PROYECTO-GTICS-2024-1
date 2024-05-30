package com.example.webapp.controller;

import com.example.webapp.entity.*;
import com.example.webapp.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

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

    public PacienteController(MedicamentosRepository medicamentosRepository,
                              UsuarioRepository usuarioRepository,
                              SedeRepository sedeRepository,
                              PedidosPacienteRepository pedidosPacienteRepository,
                              CarritoRepository carritoRepository,
                              PedidosPacienteRecojoRepository pedidosPacienteRecojoRepository,
                              MedicamentosRecojoRepository medicamentosRecojoRepository,
                              MedicamentosDelPedidoRepository medicamentosDelPedidoRepository) {

        this.medicamentosRepository = medicamentosRepository;
        this.sedeRepository = sedeRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidosPacienteRepository = pedidosPacienteRepository;
        this.carritoRepository = carritoRepository;
        this.pedidosPacienteRecojoRepository = pedidosPacienteRecojoRepository;
        this.medicamentosRecojoRepository = medicamentosRecojoRepository;
        this.medicamentosDelPedidoRepository = medicamentosDelPedidoRepository;
    }
    /*---------------------------------------*/


    /*QRUD y vista de PREORDENES*/
    @GetMapping(value = {"/paciente/inicio", "/paciente"})
    public String listarPreordenes(Model model) {

        int usuid = 29;
        List<Carrito> tamanocarrito = carritoRepository.listarCarrito();
        model.addAttribute("tamañoCarrito", tamanocarrito.size());
        model.addAttribute("listaPedidosPreorden", pedidosPacienteRepository.findAll());
        List<String> tamanolista = pedidosPacienteRepository.pedidosPreorden(usuid);
        int lleno = 1;
        if (tamanolista.isEmpty()) {
            lleno = 0;
        }
        model.addAttribute("lleno", lleno);
        return "paciente/inicio";
    }
    /*---------------------------------------*/

    /*QRUD y vista de MEDICAMENTOS*/

    /*---------------------------------------*/



    /*QRUD y vista del CARRITO*/

    /*---------------------------------------*/



    /*QRUD y vista del FORM*/

    /*---------------------------------------*/



    /*QRUD y vista de MIS PEDIDOS*/

    /*---------------------------------------*/



    /*QRUD y vista de ESTADO DEL PEDIDO*/

    /*---------------------------------------*/


}
