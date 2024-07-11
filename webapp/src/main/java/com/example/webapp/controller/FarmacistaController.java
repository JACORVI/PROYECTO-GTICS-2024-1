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
}
