package com.example.webapp.controller.superadmin;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Usuario;
import com.example.webapp.repository.MedicamentosRepository;
import com.example.webapp.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/superadmin")
public class SuperadminController {

    final
    MedicamentosRepository medicamentosRepository;
    UsuarioRepository usuarioRepository;

    public SuperadminController(MedicamentosRepository medicamentosRepository,
                                UsuarioRepository usuarioRepository) {

        this.medicamentosRepository = medicamentosRepository;

        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("")
    public String Plantilla() {
        return "superadmin/Plantilla";
    }

    @GetMapping("/Estado_Envios")
    public String Estado_Envios() {
        return "superadmin/Plantilla_Vista_Estado_Envios";
    }

    @GetMapping("/Estado_Solicitudes_Farmacistas")
    public String Estado_Solicitudes_Farmacistas() {
        return "superadmin/Plantilla_Vista_Estado_Solicitudes_Farmacistas";
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
        List<Medicamentos> lista = medicamentosRepository.findAll();
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
            model.addAttribute("medicamento", medicamento);
            return "superadmin/Plantilla_Vista_Ver_Medicamento";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Medicamentos";
        }
    }

    //Buscar Medicamento por Nombre
    @PostMapping("/buscarPorNombre")
    public String buscarPorNombre(@RequestParam("searchField") String searchField, Model model) {

        List<Medicamentos> lista = medicamentosRepository.findByNombre(searchField);
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
            model.addAttribute("medicamento", medicamento);
            return "superadmin/Plantilla_Vista_Actualizar_Medicamento";
        } else {
            return "redirect:/superadmin/Plantilla_Vista_Medicamentos";
        }
    }

    //Guardar Medicamento
    @PostMapping("/Guardar_Medicamento")
    public String guardarNuevoMedicamento(Medicamentos medicamento) {
        medicamentosRepository.save(medicamento);
        return "redirect:/superadmin/Medicamentos";
    }

    //Eliminar Medicamento
    @GetMapping("/Eliminar_Medicamento")
    public String borrarTransportista(@RequestParam("id") int id) {

        Optional<Medicamentos> optMedicamento = medicamentosRepository.findById(id);

        if (optMedicamento.isPresent()) {
            medicamentosRepository.deleteById(id);
        }
        return "redirect:/superadmin/Medicamentos";

    }
    //------------------------------------------------------------------------------------------------------------------

    //Listar Usuarios
    @GetMapping("/Vista_Principal")
    public String Usuarios(Model model) {
        List<Usuario> lista = usuarioRepository.findByRol("Doctor");
        model.addAttribute("listTransportation", lista);
        List<Usuario> lista1 = usuarioRepository.findByRol("Administrador");
        model.addAttribute("listTransportation1", lista1);
        List<Usuario> lista2 = usuarioRepository.findByRol("Farmacista");
        model.addAttribute("listTransportation2", lista2);
        List<Usuario> lista3 = usuarioRepository.findByRol("Paciente");
        model.addAttribute("listTransportation3", lista3);
        return "superadmin/Plantilla_Vista_Principal";
    }

    @PostMapping("/Guardar_Usuario")
    public String guardar_Doctor(Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/Vista_Principal";
    }

    @GetMapping("/Eliminar_Usuario")
    public String borrarDoctor(@RequestParam("id") int id) {

        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()) {
            usuarioRepository.deleteById(id);
        }
        return "redirect:/superadmin/Vista_Principal";

    }

    //CRUD DOCTOR
    @GetMapping("/Ver_Doctor")
    public String Ver_Doctor(Model model,
                             @RequestParam("id") int id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
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
            model.addAttribute("usuario", usuario);
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


}

