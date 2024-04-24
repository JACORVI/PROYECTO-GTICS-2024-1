package com.example.webapp.controller.superadmin;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.repository.MedicamentosRepository;
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

    final MedicamentosRepository medicamentosRepository;

    public SuperadminController(MedicamentosRepository medicamentosRepository){
        this.medicamentosRepository = medicamentosRepository;
    }


    @GetMapping("")
    public String Plantilla() {
        return "superadmin/Plantilla";
    }
    @GetMapping("/Actualizar_Administrador")
    public String Actualizar_Administrador() {
        return "superadmin/Plantilla_Vista_Actualizar_Administrador";
    }
    @GetMapping("/Actualizar_Doctor")
    public String Actualizar_Doctor() {
        return "superadmin/Plantilla_Vista_Actualizar_Doctor";
    }
    @GetMapping("/Actualizar_Farmacista")
    public String Actualizar_Farmacista() {
        return "superadmin/Plantilla_Vista_Actualizar_Farmacista";
    }
    @GetMapping("/Actualizar_Medicamento")
    public String Actualizar_Medicamento() {
        return "superadmin/Plantilla_Vista_Actualizar_Medicamento";
    }
    @GetMapping("/Actualizar_Paciente")
    public String Actualizar_Paciente() {
        return "superadmin/Plantilla_Vista_Actualizar_Paciente";
    }
    @GetMapping("/Estado_Envios")
    public String Estado_Envios() {
        return "superadmin/Plantilla_Vista_Estado_Envios";
    }
    @GetMapping("/Estado_Solicitudes_Farmacistas")
    public String Estado_Solicitudes_Farmacistas() {
        return "superadmin/Plantilla_Vista_Estado_Solicitudes_Farmacistas";
    }





    @GetMapping("/Medicamentos")
    public String Medicamentos(Model model){
        List<Medicamentos> lista = medicamentosRepository.findAll();
        model.addAttribute("listTransportation",lista);
        return "superadmin/Plantilla_Vista_Medicamentos";
    }


    @GetMapping("/Vista_Principal")
    public String Vista_Principal() {
        return "superadmin/Plantilla_Vista_Principal";
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
    @GetMapping("/Ver_Administrador")
    public String Ver_Administrador() {
        return "superadmin/Plantilla_Vista_Ver_Administrador";
    }
    @GetMapping("/Ver_Doctor")
    public String Ver_Doctor() {
        return "superadmin/Plantilla_Vista_Ver_Doctor";
    }
    @GetMapping("/Ver_Farmacista")
    public String Ver_Farmacista() {
        return "superadmin/Plantilla_Vista_Ver_Farmacista";
    }



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




    @GetMapping("/Ver_Paciente")
    public String Ver_Paciente() {
        return "superadmin/Plantilla_Vista_Ver_Paciente";
    }
    @GetMapping("/Ver_Perfil")
    public String Ver_Perfil() {
        return "superadmin/Perfil";
    }
    @GetMapping("/Cerrar_Cuenta")
    public String Cerrar_Cuenta() {
        return "superadmin/Index";
    }

    @PostMapping("/buscarPorNombre")
    public String buscarPorNombre(@RequestParam("searchField") String searchField, Model model) {

        List<Medicamentos> lista = medicamentosRepository.findByNombre(searchField);
        model.addAttribute("listTransportation", lista);
        model.addAttribute("textoBuscado", searchField);
        return "superadmin/Plantilla_Vista_Medicamentos";
    }
}

