package com.example.webapp.controller;

import com.example.webapp.entity.Usuario;
import com.example.webapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
@Controller
public class LoginController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    //@GetMapping("/openLoginWindow")
    //public String loginWindow(){
    //    return "loginWindow";}


    @GetMapping("/login")
    public String loginWindow(){
        return "sistema/Index";
    }

    @PostMapping("/registro/usuario")
    public String registrarUsuario(@ModelAttribute("usuario") @Valid Usuario usuario,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en el formulario de registro. Por favor, revisa los campos.");
            return "redirect:/login";
        }

        try {
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Registro exitoso. En un plazo máximo de 48h recibirás una notificación sobre el estado de tu registro.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un problema al registrar el usuario. Por favor, inténtalo de nuevo.");
        }

        return "redirect:/login";
    }

    @ModelAttribute("usuario")
    public Usuario usuario() {
        return new Usuario();
    }
}
