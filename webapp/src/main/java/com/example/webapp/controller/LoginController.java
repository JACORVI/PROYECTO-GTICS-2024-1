package com.example.webapp.controller;

import com.example.webapp.dao.DataDao;
import com.example.webapp.entity.Data;
import com.example.webapp.entity.Roles;
import com.example.webapp.entity.Usuario;
import com.example.webapp.repository.UsuarioRepository;
import com.example.webapp.util.Correo;
import com.example.webapp.util.Utileria;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    final DataDao dataDao;

    public LoginController(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Correo correo;

    @Autowired
    private Utileria util;
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping(value = {"/acceso"})
    public String Login(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String rol = "";
        for (GrantedAuthority role : auth.getAuthorities()) {
            rol = role.getAuthority();
            break;
        }

        if (rol.equalsIgnoreCase("Paciente")) {
            return "redirect:/paciente/inicio";
        }
        if (rol.equalsIgnoreCase("Superadmin")) {
            return "redirect:/superadmin/";
        }
        if (rol.equalsIgnoreCase("Farmacista")) {
            return "redirect:/farmacista/nuevopedido";
        }
        if (rol.equalsIgnoreCase("Admin")) {
            return "redirect:/admin/medicamentos";
        }
        return "/login";
    }

    @GetMapping("/reestablecer/generar_token")
    @ResponseBody
    public Map<String, String> recuperarPassword(String correo) {
        List<Usuario> usuarios = usuarioRepository.buscarPorCorreo(correo);
        int result = 0;
        Map<String, String> response = new HashMap<>();

        try {
            if (usuarios.size() > 0) {
                for (Usuario obj : usuarios) {

                    if (obj.getCuenta_activada() == 1) {
                        obj.setToken_recuperacion(util.GenerarToken());

                        String cuerpo = this.correo.construirCuerpoRecuperarPassword(obj);
                        boolean envio = this.correo.EnviarCorreo("Reestablecer contraseña", cuerpo, obj);
                        if (envio) {
                            result = usuarioRepository.actualizarFechaYTokenRecuperacion(obj.getToken_recuperacion(),
                                    obj.getId());
                        }

                        if (result > 0) {
                            response.put("response", "OK");
                        } else {
                            response.put("response", "No se pudo enviar correo para recuperar contraseña.");
                        }
                    } else {
                        response.put("response", "Lo sentimos! Su cuenta no se encuentra activada.");
                    }
                }
            } else {
                response.put("response", "El correo proporcionado no se encuentra registrado!");
            }
        } catch (Exception ex) {
            response.put("error", ex.getMessage());
        }

        return response;
    }

    @GetMapping("/reestablecer/password")
    public String VistaNuevoPassword(@RequestParam(name = "token", required = false) String token,
                                     RedirectAttributes attributes, Model model) {
        if (token == null || token.isEmpty()) {
            attributes.addFlashAttribute("error", "Incorrecto! El token de acceso no es valido!");
            return "redirect:/login";
        }
        Usuario objUsu = usuarioRepository.buscarPorToken(token);

        if (objUsu == null) {
            attributes.addFlashAttribute("error", "Token incorrecto!");
            return "redirect:/login";
        }

        String msg = usuarioRepository.validarToken(objUsu.getId(), token);

        if (!msg.equals("OK")) {
            attributes.addFlashAttribute("error", msg);
            return "redirect:/login";
        }

        model.addAttribute("mensaje", msg);
        model.addAttribute("usuario", objUsu);

        return "sistema/HomeNuevoPassword";
    }

    @GetMapping("/reestablecer/password/save")
    @ResponseBody
    public Map<String, String> GuardarPassword(@RequestParam(name = "password", required = true) String password,
                                               int id,
                                               RedirectAttributes attributes, Model model) {
        Map<String, String> response = new HashMap<>();
        try {
            password = encoder.encode(password);
            int result = usuarioRepository.actualizarPassword(password, id);

            if (result > 0) {
                response.put("response", "OK");
            } else {
                response.put("response", "No se pudo reestablecer contraseña");
            }

        } catch (Exception ex) {
            response.put("response", ex.getMessage());
        }

        return response;
    }

    @GetMapping(value = {"/login", "/", ""})
    public String loginWindow(Authentication auth) {
        try {
            Usuario usuario = usuarioRepository.findByCorreo(auth.getName());
            String rol = usuario.getRol().getNombre();
            if (rol.equals("Paciente")) {
                return "redirect:/paciente/inicio";
            }
            if (rol.equals("Superadmin")) {
                return "redirect:/superadmin/";
            }
            if (rol.equals("Farmacista")) {
                return "redirect:/farmacista/nuevopedido";
            }
            if (rol.equals("Admin")) {
                return "redirect:/admin/medicamentos";
            }
        }
        catch (Exception ex) {
            return "sistema/Index";
        }
        return "sistema/Index";
    }

    @PostMapping("/validarDNI")
    public String validarPersona(@ModelAttribute("dni") String dni, RedirectAttributes redirectAttributes, Model model) {
        try {
            Data data = dataDao.buscarPorDni(dni);
            return "redirect:/registro?dni=" + dni;
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "El DNI ingresado no es valido.");
            System.out.println(e.getMessage());
            return "redirect:/login";
        }
    }


    @GetMapping("/registro")
    public String registerWindow(@RequestParam("dni") String dni, RedirectAttributes redirectAttributes, Model model) {
        try {
            Data data = dataDao.buscarPorDni(dni);

            //NOMBRES -> Nombres
            String nombreCompleto = data.getNombres();
            String[] palabras1 = nombreCompleto.split(" ");
            StringBuilder nombreFormateado = new StringBuilder();
            for (String palabra : palabras1) {
                if (!nombreFormateado.toString().isEmpty()) {
                    nombreFormateado.append(" ");
                }
                nombreFormateado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase());
            }

            //APELLIDOS -> Apellidos
            String apellidoCompleto = data.getApellido_paterno() + " " + data.getApellido_materno();
            String[] palabras2 = apellidoCompleto.split(" ");
            StringBuilder apellidoFormateado = new StringBuilder();
            for (String palabra : palabras2) {
                if (!apellidoFormateado.toString().isEmpty()) {
                    apellidoFormateado.append(" ");
                }
                apellidoFormateado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase());
            }

            model.addAttribute("nombresValidados", nombreFormateado.toString());
            model.addAttribute("apellidosValidados", apellidoFormateado.toString());
            model.addAttribute("dniValidado", data.getDni());
            return "sistema/FormRegistro";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "El DNI ingresado no es valido.");
            return "redirect:/login";
        }
    }

    @PostMapping("/registro/usuario")
    public String registrarUsuario(@ModelAttribute("usuario") @Valid Usuario usuario,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        Roles rol = new Roles();
        rol.setId(4); // ROL PACIENTE

        usuario.setFecha_creacion(new Date());
        usuario.setCodigo_colegiatura("Sin-Codigo");
        usuario.setRol(rol);
        usuario.setContrasena("");
        logger.info(usuario.toString());
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                logger.error("Error de validación en el campo {}: {}", fieldName, errorMessage);
            }
            redirectAttributes.addFlashAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("error", "Error en el formulario de registro. Por favor, revisa los campos.");
            return "redirect:/login";
        }

        try {
            usuario.setToken_recuperacion(util.GenerarToken()); // Token de ACTIVACION
            usuario.setCuenta_activada(0);
            usuarioRepository.save(usuario);

            String cuerpo = this.correo.construirCuerpoActivarCuenta(usuario);
            boolean envio = this.correo.EnviarCorreo("Activar cuenta", cuerpo, usuario);

            redirectAttributes.addFlashAttribute("success", "Registro exitoso. Se te ha enviado un correo de notificación sobre el estado de tu registro para su activación.");
        } catch (Exception e) {
            logger.error("Error al registrar el usuario", e);
            redirectAttributes.addFlashAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("error", "Hubo un problema al registrar el usuario. Por favor, inténtalo de nuevo.");
        }

        return "redirect:/login";
    }

    @GetMapping("/reestablecer/activar")
    public String VistaActivarCuentaPassword(@RequestParam(name = "token", required = false) String token,
                                             RedirectAttributes attributes, Model model) {
        if (token == null || token.isEmpty()) {
            attributes.addFlashAttribute("error", "Incorrecto! El token de acceso no es valido!");
            return "redirect:/login";
        }
        Usuario objUsu = usuarioRepository.buscarPorToken(token);

        if (objUsu == null) {
            attributes.addFlashAttribute("error", "Su token es invalido o su cuenta ya ha sido reestablecida!");
            return "redirect:/login";
        }

        if (objUsu.getCuenta_activada() == 1) {
            attributes.addFlashAttribute("error", "Su cuenta ya se encuentra activada!");
            return "redirect:/login";
        }

        model.addAttribute("usuario", objUsu);

        return "sistema/ActivarNuevoPassword";
    }

    @GetMapping("/reestablecer/activar/save")
    @ResponseBody
    public Map<String, String> GuardarActivarPassword(@RequestParam(name = "password", required = true) String password,
                                                      int id,
                                                      RedirectAttributes attributes, Model model) {
        Map<String, String> response = new HashMap<>();
        try {
            password = encoder.encode(password);
            int result = usuarioRepository.actualizarPasswordyEstado(password, id);

            if (result > 0) {
                response.put("response", "OK");
            } else {
                response.put("response", "No se pudo activar cuenta");
            }

        } catch (Exception ex) {
            response.put("response", ex.getMessage());
        }

        return response;
    }

    @ModelAttribute("usuario")
    public Usuario usuario() {
        return new Usuario();
    }


}
