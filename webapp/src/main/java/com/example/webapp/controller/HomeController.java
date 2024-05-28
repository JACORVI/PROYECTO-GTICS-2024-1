package com.example.webapp.controller;

import com.example.webapp.entity.Usuario;
import com.example.webapp.repository.UsuarioRepository;
import com.example.webapp.util.Utils;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private Utils util;

    @GetMapping("/")
    public String Login(Model model, Authentication auth) {
        log.info("Ingreso login..");

        if (auth != null && auth.isAuthenticated()) {
            log.info("esta autentificado..");
            return "redirect:/acceso";
        }

        model.addAttribute("usuario", new Usuario());
        return "sistema/Index";
    }

    @GetMapping("/acceso")
    public String Acceso(Model model, Authentication auth) {
        log.info("Ingreso acceso..");
        String rol = util.obtenerRol(auth);
        String username = util.obtenerUsername(auth);

        if (rol != null) {
            log.info("Rol obtenido: " + rol); // Agregar log para verificar el rol obtenido
            List<Usuario> listUsu = usuarioRepository.buscarPorCorreo(username);

            if (!listUsu.isEmpty()) {
                session.setAttribute("avatar", listUsu.get(0).getImagen());
                session.setAttribute("usuario", listUsu.get(0));
            }

            switch (rol) {
                case "Farmacista":
                    log.info("Redirigiendo a Farmacista...");
                    return "redirect:/farmacista/nuevopedido";
                case "Superadmin":
                    log.info("Redirigiendo a Superadmin...");
                    return "redirect:/superadmin/";
                case "Paciente":
                    log.info("Redirigiendo a Paciente...");
                    return "redirect:/paciente/inicio";
                case "Administrador":
                    log.info("Redirigiendo a Administrador...");
                    return "redirect:/admin/medicamentos";
                default:
                    log.info("Rol no reconocido: " + rol);
                    break;
            }
        } else {
            log.info("Rol obtenido es nulo");
        }

        log.info("No cuenta con rol asignado..");
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String Logout(Model model) {
        return "redirect:/login";
    }

    @PostMapping("/registro/usuario")
    public String Registro(Model model, Usuario usuario, RedirectAttributes attributes) {
        try {
            if (usuarioRepository.buscarPorCorreo(usuario.getCorreo().trim()).size() == 0) {
                usuarioRepository.save(usuario);

                if (usuario.getId() > 0) {
                    attributes.addFlashAttribute("success", "Usuario registrado!");
                    return "redirect:/";
                }
                model.addAttribute("error", "No se pudo registrar usuario!");
            } else {
                model.addAttribute("error", "El correo " + usuario.getCorreo() + " ya se encuentra registrado!");
            }
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            ex.printStackTrace();
        }

        model.addAttribute("usuario", usuario);
        return "sistema/Index";
    }

    @GetMapping("/perfil")
    public String Ver_Perfil(Model model, Authentication auth) {
        String username = util.obtenerUsername(auth);
        List<Usuario> listUsu = usuarioRepository.buscarPorCorreo(username);
        Usuario obj = listUsu.get(0);

        if (obj == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", obj);
        return "sistema/Perfil";
    }

    @PostMapping("/perfil/guardar")
    public String GuardarPerfil(Model model, Usuario obj,
            RedirectAttributes attributes,
            @RequestParam("accion") String accion
    ) {
        Usuario data = null;
        try {
            data = usuarioRepository.findById(obj.getId()).orElse(null);

            if (accion.equalsIgnoreCase("perfil")) {
                data.setNombres(obj.getNombres().trim());
                data.setApellidos(obj.getApellidos().trim());
                data.setTelefono(obj.getTelefono().trim());
                data.setDni(obj.getDni());
                data.setCorreo(obj.getCorreo().trim());

                if (obj.getContrasena() != null && obj.getContrasena().trim().length() > 0) {
                    data.setContrasena(obj.getContrasena().trim());
                }
            }

            if (accion.equalsIgnoreCase("avatar")) {
                data.setImagen(obj.getImagen().trim());
            }

            if (accion.equalsIgnoreCase("direccion")) {
                data.setReferencia(obj.getReferencia().trim());
                data.setDistrito(obj.getDistrito().trim());
                data.setDireccion(obj.getDireccion().trim());
            }

            usuarioRepository.save(data);

            if (obj.getId() > 0) {
                attributes.addFlashAttribute("success", "Datos actualizados!!");
                session.setAttribute("avatar", data.getImagen());
                return "redirect:/perfil";
            }
            model.addAttribute("error", "No se pudo actualizar datos!");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            ex.printStackTrace();
        }

        model.addAttribute("usuario", data);
        return "sistema/Perfil";
    }

}
