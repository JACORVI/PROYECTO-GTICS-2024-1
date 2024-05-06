package com.example.webapp.controller.superadmin;

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
@RequestMapping("/superadmin")
public class SuperadminController {

    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;
    PedidosReposicionRepository pedidosReposicionRepository;
    UsuarioHasSedeRepository usuarioHasSedeRepository;
    SedeRepository sedeRepository;
    SedeHasMedicamentosRepository sedeHasMedicamentosRepository;

    public SuperadminController(MedicamentosRepository medicamentosRepository,
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


    @GetMapping("")
    public String Plantilla() {
        return "superadmin/Plantilla";
    }

    @GetMapping("/Registrar_Medicamento")
    public String Registrar_Medicamento() {
        return "superadmin/Plantilla_Vista_Registrar_Medicamento";
    }

    @GetMapping("/Registro_Administrador")
    public String Registro_Administrador() {
        return "superadmin/Plantilla_Vista_Registro_Administrador";
    }

    @GetMapping("/Registro_Doctor")
    public String Registro_Doctor() {
        return "superadmin/Plantilla_Vista_Registro_Doctor";
    }

    @GetMapping("/Ver_Perfil")
    public String Ver_Perfil() {
        return "superadmin/Perfil";
    }

    @GetMapping("/Cerrar_Cuenta")
    public String Cerrar_Cuenta() {
        return "superadmin/Index";
    }


    //------------------------------------------------------------------------------------------------------------------
    //Listar Medicamentos
    @GetMapping("/Medicamentos")
    public String Medicamentos(Model model) {
        List<Medicamentos> lista = medicamentosRepository.buscarMedicamentoGeneral(0);
        model.addAttribute("listTransportation", lista);
        return "superadmin/Plantilla_Vista_Medicamentos";
    }

    //Ver Medicamento
    @GetMapping("/Ver_Medicamento")
    public String Ver_Medicamento(Model model,
                                  @RequestParam("id") int id) {
        Optional<Medicamentos> optMedicamento = medicamentosRepository.findById(id);

        if (optMedicamento.isPresent()) {
            Medicamentos medicamento = optMedicamento.get();
            List<SedeHasMedicamentos> list = sedeHasMedicamentosRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (SedeHasMedicamentos sedeHasMedicamentos : list) {
                    if (sede.getId() == sedeHasMedicamentos.getId_sede().getId()) {
                        if (sedeHasMedicamentos.getId_medicamentos().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }

            System.out.println(listaIndicador);
            model.addAttribute("medicamento", medicamento);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);


            return "superadmin/Plantilla_Vista_Ver_Medicamento";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Medicamentos";
        }
    }


    //Buscar Medicamento por Nombre
    @PostMapping("/buscarPorNombre")
    public String buscarPorNombre(@RequestParam("searchField") String searchField, Model model) {

        List<Medicamentos> lista = medicamentosRepository.buscarMedicamento(searchField);
        model.addAttribute("listTransportation", lista);
        model.addAttribute("textoBuscado", searchField);
        return "superadmin/Plantilla_Vista_Medicamentos";
    }

    //Editar Medicamento
    @GetMapping("/Editar_Medicamento")
    public String editarMedicamento(Model model,
                                    @RequestParam("id") int id) {

        Optional<Medicamentos> optMedicamento = medicamentosRepository.findById(id);
        if (optMedicamento.isPresent()) {
            Medicamentos medicamento = optMedicamento.get();
            List<SedeHasMedicamentos> list = sedeHasMedicamentosRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (SedeHasMedicamentos sedeHasMedicamentos : list) {
                    if (sede.getId() == sedeHasMedicamentos.getId_sede().getId()) {
                        if (sedeHasMedicamentos.getId_medicamentos().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }

            System.out.println(listaIndicador);
            model.addAttribute("medicamento", medicamento);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);


            return "superadmin/Plantilla_Vista_Actualizar_Medicamento";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Medicamentos";
        }
    }

    //Guardar Medicamento
    @PostMapping("/Guardar_Medicamento")
    public String guardarNuevoMedicamento(Medicamentos medicamento, Model model,@RequestParam("id") int id) {

        medicamentosRepository.save(medicamento);

        Optional<Medicamentos> optMedicamento = medicamentosRepository.findById(id);

        if (optMedicamento.isPresent()) {
            Medicamentos medicamento1 = optMedicamento.get();
            List<SedeHasMedicamentos> list = sedeHasMedicamentosRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (SedeHasMedicamentos sedeHasMedicamentos : list) {
                    if (sede.getId() == sedeHasMedicamentos.getId_sede().getId()) {
                        if (sedeHasMedicamentos.getId_medicamentos().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }

            System.out.println(listaIndicador);
            model.addAttribute("medicamento", medicamento1);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);


            return "superadmin/Plantilla_Vista_Ver_Medicamento";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Medicamentos";
        }
    }

    @GetMapping("/Asignar_Sede_Medicamento")
    public String AsignarMedicamento(Model model,
                          @RequestParam("id") int id,
                          @RequestParam("idsede") int idSede) {

        sedeHasMedicamentosRepository.AsignarSedeMedicamento(idSede,id);
        Optional<Medicamentos> optMedicamento = medicamentosRepository.findById(id);
        if (optMedicamento.isPresent()) {
            Medicamentos medicamento = optMedicamento.get();
            List<SedeHasMedicamentos> list = sedeHasMedicamentosRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (SedeHasMedicamentos sedeHasMedicamentos : list) {
                    if (sede.getId() == sedeHasMedicamentos.getId_sede().getId()) {
                        if (sedeHasMedicamentos.getId_medicamentos().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }

            System.out.println(listaIndicador);
            model.addAttribute("medicamento", medicamento);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);


            return "superadmin/Plantilla_Vista_Actualizar_Medicamento";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Medicamentos";
        }

    }

    @GetMapping("/No_Asignar_Sede_Medicamento")
    public String NoAsignarMedicamento(Model model,
                                     @RequestParam("id") int id,
                                     @RequestParam("idsede") int idSede) {

        sedeHasMedicamentosRepository.NoAsignarSedeMedicamento(idSede,id);
        Optional<Medicamentos> optMedicamento = medicamentosRepository.findById(id);
        if (optMedicamento.isPresent()) {
            Medicamentos medicamento = optMedicamento.get();
            List<SedeHasMedicamentos> list = sedeHasMedicamentosRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (SedeHasMedicamentos sedeHasMedicamentos : list) {
                    if (sede.getId() == sedeHasMedicamentos.getId_sede().getId()) {
                        if (sedeHasMedicamentos.getId_medicamentos().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }

            System.out.println(listaIndicador);
            model.addAttribute("medicamento", medicamento);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);


            return "superadmin/Plantilla_Vista_Actualizar_Medicamento";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Medicamentos";
        }

    }

    //Eliminar Medicamento
    @GetMapping("/Eliminar_Medicamento")
    public String borrarTransportista(@RequestParam("id") int id) {

        Optional<Medicamentos> optMedicamento = medicamentosRepository.findById(id);

        if (optMedicamento.isPresent()) {
            medicamentosRepository.borradoLogico(1,id);
        }
        return "redirect:/superadmin/Medicamentos";

    }


    //------------------------------------------------------------------------------------------------------------------

    //Listar Usuarios
    @GetMapping("/Vista_Principal")
    public String Usuarios(Model model) {
        List<Usuario> lista = usuarioRepository.buscarDoctor("Doctor",0);
        model.addAttribute("listTransportation", lista);
        List<Usuario> lista1 = usuarioRepository.buscarAdministrador("Administrador",0);
        model.addAttribute("listTransportation1", lista1);
        List<Usuario> lista2 = usuarioRepository.buscarFarmacista("Farmacista",0);
        model.addAttribute("listTransportation2", lista2);
        List<Usuario> lista3 = usuarioRepository.buscarPaciente("Paciente",0);
        model.addAttribute("listTransportation3", lista3);
        return "superadmin/Plantilla_Vista_Principal";
    }

    @PostMapping("/Guardar_Usuario")
    public String guardar_Doctor(Usuario usuario,@RequestParam("id") int id, Model model) {

        usuarioRepository.save(usuario);

        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if (optUsuario.isPresent()) {
            Usuario usuario1 = optUsuario.get();
            if(usuario1.getRol().equals("Doctor")) {
                List<UsuarioHasSede> list = usuarioHasSedeRepository.findAll();
                List<Sede> list1 = sedeRepository.findAll();

                List<String> listaIndicador = new ArrayList<>();

                for (Sede sede : list1) {
                    int i = 0;
                    for (UsuarioHasSede usuarioHasSede : list) {
                        if (sede.getId() == usuarioHasSede.getSede_id_sede().getId()) {
                            if (usuarioHasSede.getUsuario_id_usario().getId() == id) {
                                i=1;
                            }
                        }
                    }
                    if (i==0){
                        listaIndicador.add("NoAsignado");
                    }else{
                        listaIndicador.add("Asignado");
                    }
                }
                System.out.println(listaIndicador);
                model.addAttribute("usuario", usuario1);
                model.addAttribute("ListaIndicador", listaIndicador);
                model.addAttribute("ListaSedes",list1);
                return "superadmin/Plantilla_Vista_Ver_Doctor";
            }
            if(usuario1.getRol().equals("Administrador")) {
                model.addAttribute("usuario", usuario1);
                return "superadmin/Plantilla_Vista_Ver_Administrador";
            }
            if(usuario1.getRol().equals("Farmacista")) {
                model.addAttribute("usuario", usuario1);
                return "superadmin/Plantilla_Vista_Ver_Farmacista";
            }
            if(usuario1.getRol().equals("Paciente")) {
                model.addAttribute("usuario", usuario1);
                return "superadmin/Plantilla_Vista_Ver_Paciente";
            }
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Principal";
        }
        return "redirect:/superadmin/Vista_Principal";
    }

    @GetMapping("/Eliminar_Usuario")
    public String borrarDoctor(@RequestParam("id") int id) {

        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()) {
            usuarioRepository.borradoLogico(1,id);
        }
        return "redirect:/superadmin/Vista_Principal";

    }

    @GetMapping("/Asignar_Sede")
    public String Asignar(Model model,
                            @RequestParam("id") int id,
                            @RequestParam("idsede") int idSede) {

        usuarioHasSedeRepository.AsignarSede(id,idSede);
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        Usuario usuario1 = usuario.get();
        if(usuario1.getRol().equals("Doctor")) {
            List<UsuarioHasSede> list = usuarioHasSedeRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (UsuarioHasSede usuarioHasSede : list) {
                    if (sede.getId() == usuarioHasSede.getSede_id_sede().getId()) {
                        if (usuarioHasSede.getUsuario_id_usario().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }
            System.out.println(listaIndicador);
            model.addAttribute("usuario", usuario1);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);
            return "superadmin/Plantilla_Vista_Actualizar_Doctor";
        }
        if(usuario1.getRol().equals("Administrador")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Administrador";
        }
        if(usuario1.getRol().equals("Farmacista")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Farmacista";
        }
        if(usuario1.getRol().equals("Paciente")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Paciente";
        }
        return  "superadmin/Vista_Principal";

    }

    @GetMapping("/No_Asignar_Sede")
    public String NoAsignar(Model model,
                               @RequestParam("id") int id,
                               @RequestParam("idsede") int idSede) {

        usuarioHasSedeRepository.NoAsignarSede(id,idSede);
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        Usuario usuario1 = usuario.get();
        if(usuario1.getRol().equals("Doctor")) {
            List<UsuarioHasSede> list = usuarioHasSedeRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (UsuarioHasSede usuarioHasSede : list) {
                    if (sede.getId() == usuarioHasSede.getSede_id_sede().getId()) {
                        if (usuarioHasSede.getUsuario_id_usario().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }
            System.out.println(listaIndicador);
            model.addAttribute("usuario", usuario1);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);
            return "superadmin/Plantilla_Vista_Actualizar_Doctor";
        }
        if(usuario1.getRol().equals("Administrador")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Administrador";
        }
        if(usuario1.getRol().equals("Farmacista")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Farmacista";
        }
        if(usuario1.getRol().equals("Paciente")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Paciente";
        }
        return  "superadmin/Vista_Principal";

    }

    @GetMapping("/Pasar_Activo")
    public String Pasar_Activo(Model model,
                             @RequestParam("id") int id) {
            usuarioRepository.pasarActivo("Activo",id);
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            Usuario usuario1 = usuario.get();
            if(usuario1.getRol().equals("Doctor")) {
                List<UsuarioHasSede> list = usuarioHasSedeRepository.findAll();
                List<Sede> list1 = sedeRepository.findAll();

                List<String> listaIndicador = new ArrayList<>();

                for (Sede sede : list1) {
                    int i = 0;
                    for (UsuarioHasSede usuarioHasSede : list) {
                        if (sede.getId() == usuarioHasSede.getSede_id_sede().getId()) {
                            if (usuarioHasSede.getUsuario_id_usario().getId() == id) {
                                i=1;
                            }
                        }
                    }
                    if (i==0){
                        listaIndicador.add("NoAsignado");
                    }else{
                        listaIndicador.add("Asignado");
                    }
                }
                System.out.println(listaIndicador);
                model.addAttribute("usuario", usuario1);
                model.addAttribute("ListaIndicador", listaIndicador);
                model.addAttribute("ListaSedes",list1);
                return "superadmin/Plantilla_Vista_Actualizar_Doctor";
            }
            if(usuario1.getRol().equals("Administrador")) {
                model.addAttribute("usuario", usuario1);
                return "superadmin/Plantilla_Vista_Actualizar_Administrador";
            }
            if(usuario1.getRol().equals("Farmacista")) {
                model.addAttribute("usuario", usuario1);
                return "superadmin/Plantilla_Vista_Actualizar_Farmacista";
            }
            if(usuario1.getRol().equals("Paciente")) {
                model.addAttribute("usuario", usuario1);
                return "superadmin/Plantilla_Vista_Actualizar_Paciente";
            }
            return  "superadmin/Vista_Principal";
    }

    @GetMapping("/Pasar_Inactivo")
    public String Pasar_Inactivo(Model model,
                               @RequestParam("id") int id) {
        usuarioRepository.pasarInactivo("Inactivo",id);
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        Usuario usuario1 = usuario.get();
        if(usuario1.getRol().equals("Doctor")) {
            List<UsuarioHasSede> list = usuarioHasSedeRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (UsuarioHasSede usuarioHasSede : list) {
                    if (sede.getId() == usuarioHasSede.getSede_id_sede().getId()) {
                        if (usuarioHasSede.getUsuario_id_usario().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }
            System.out.println(listaIndicador);
            model.addAttribute("usuario", usuario1);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);
            return "superadmin/Plantilla_Vista_Actualizar_Doctor";
        }
        if(usuario1.getRol().equals("Administrador")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Administrador";
        }
        if(usuario1.getRol().equals("Farmacista")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Farmacista";
        }
        if(usuario1.getRol().equals("Paciente")) {
            model.addAttribute("usuario", usuario1);
            return "superadmin/Plantilla_Vista_Actualizar_Paciente";
        }
        return  "superadmin/Vista_Principal";
    }

    //CRUD DOCTOR
    @GetMapping("/Ver_Doctor")
    public String Ver_Doctor(Model model,
                             @RequestParam("id") int id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            List<UsuarioHasSede> list = usuarioHasSedeRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (UsuarioHasSede usuarioHasSede : list) {
                    if (sede.getId() == usuarioHasSede.getSede_id_sede().getId()) {
                        if (usuarioHasSede.getUsuario_id_usario().getId() == id) {
                        i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }
            System.out.println(listaIndicador);
            model.addAttribute("usuario", usuario);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);
            return "superadmin/Plantilla_Vista_Ver_Doctor";

        } else {
            return "redirect:/superadmin/Plantilla_Vista_Principal";
        }
    }

    @GetMapping("/Editar_Doctor")
    public String editar_Doctor(Model model,
                                @RequestParam("id") int id) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            List<UsuarioHasSede> list = usuarioHasSedeRepository.findAll();
            List<Sede> list1 = sedeRepository.findAll();

            List<String> listaIndicador = new ArrayList<>();

            for (Sede sede : list1) {
                int i = 0;
                for (UsuarioHasSede usuarioHasSede : list) {
                    if (sede.getId() == usuarioHasSede.getSede_id_sede().getId()) {
                        if (usuarioHasSede.getUsuario_id_usario().getId() == id) {
                            i=1;
                        }
                    }
                }
                if (i==0){
                    listaIndicador.add("NoAsignado");
                }else{
                    listaIndicador.add("Asignado");
                }
            }
            System.out.println(listaIndicador);
            model.addAttribute("usuario", usuario);
            model.addAttribute("ListaIndicador", listaIndicador);
            model.addAttribute("ListaSedes",list1);
            return "superadmin/Plantilla_Vista_Actualizar_Doctor";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Principal";
        }
    }


    //CRUD ADMINISTRADOR
    @GetMapping("/Ver_Administrador")
    public String Ver_Administrador(Model model,
                                    @RequestParam("id") int id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "superadmin/Plantilla_Vista_Ver_Administrador";
        } else {
            return "redirect:superadmin/Plantilla_Vista_Principal";
        }
    }

    @GetMapping("/Editar_Administrador")
    public String editar_Administrador(Model model,
                                       @RequestParam("id") int id) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            model.addAttribute("usuario", usuario);
            return "superadmin/Plantilla_Vista_Actualizar_Administrador";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Principal";
        }
    }

    @GetMapping("/Aceptar_Administrador")
    public String Aceptar_Administrador(@RequestParam("id") int id) {
        usuarioRepository.aceptarAdministrador("Aceptado",id);

        return "redirect:/superadmin/Estado_Solicitudes_Farmacistas";
    }
    @GetMapping("/Rechazar_Administrador")
    public String Rechazar_Administrador(@RequestParam("id") int id) {
        usuarioRepository.rechazarAdministrador("Rechazado",id);

        return "redirect:/superadmin/Estado_Solicitudes_Farmacistas";
    }

    //CRUD FARMACISTA
    @GetMapping("/Ver_Farmacista")
    public String Ver_Farmacista(Model model,
                                 @RequestParam("id") int id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "superadmin/Plantilla_Vista_Ver_Farmacista";
        } else {
            return "redirect:superadmin/Plantilla_Vista_Principal";
        }
    }

    @GetMapping("/Editar_Farmacista")
    public String editar_Farmacista(Model model,
                                    @RequestParam("id") int id) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            model.addAttribute("usuario", usuario);
            return "superadmin/Plantilla_Vista_Actualizar_Farmacista";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Principal";
        }
    }

    //CRUD PACIENTE
    @GetMapping("/Ver_Paciente")
    public String Ver_Paciente(Model model,
                                 @RequestParam("id") int id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            return "superadmin/Plantilla_Vista_Ver_Paciente";
        } else {
            return "redirect:superadmin/Plantilla_Vista_Principal";
        }
    }

    @GetMapping("/Editar_Paciente")
    public String editar_Paciente(Model model,
                                    @RequestParam("id") int id) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            model.addAttribute("usuario", usuario);
            return "superadmin/Plantilla_Vista_Actualizar_Paciente";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Principal";
        }
    }

    //CRUD ESTADO SOLICITUDES FARMACISTAS
    @GetMapping("/Estado_Solicitudes_Farmacistas")
    public String Estado_Solicitudes_Farmacistas(Model model) {
        List<Usuario> lista = usuarioRepository.buscarFarmacista("Farmacista",0);
        model.addAttribute("listTransportation", lista);
        return "superadmin/Plantilla_Vista_Estado_Solicitudes_Farmacistas";
    }

    //CRUD ESTADO ENVÍOS
    @GetMapping("/Estado_Envios")
    public String Estado_Envios(Model model) {
        List<PedidosReposicion> lista = pedidosReposicionRepository.findAll();
        model.addAttribute("listTransportation",lista);
        return "superadmin/Plantilla_Vista_Estado_Envios";
    }
}

