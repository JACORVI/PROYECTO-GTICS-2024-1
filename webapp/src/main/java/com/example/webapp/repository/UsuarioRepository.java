package com.example.webapp.repository;


import com.example.webapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByCorreo(String email);

    @Query(value = "select * from usuario where id_roles = ?1 and borrado_logico = ?2 and estado_solicitud = ?3 order by fecha_creacion", nativeQuery = true)
    List<Usuario> buscarFarmacistaAceptado(int rol,int borrado_logico,String estado_solicitud);

    @Query(value = "select * from usuario where id_roles = ?1 and borrado_logico = ?2 order by fecha_creacion", nativeQuery = true)
    List<Usuario> buscarFarmacista(int rol,int borrado_logico);

    @Query(value = "SELECT id_usuario FROM usuario ORDER BY id_usuario DESC LIMIT 1;\n", nativeQuery = true)
    int buscarUltimo();

    @Query(value = "select * from usuario where id_roles = ?1 and borrado_logico = ?2 ", nativeQuery = true)
    List<Usuario> buscarDoctor(int rol,int borrado_logico);

    @Query(value = "select * from usuario where id_roles = ?1 and borrado_logico = ?2 ", nativeQuery = true)
    List<Usuario> buscarAdministrador(int rol,int borrado_logico);

    @Query(value = "select * from usuario where id_roles = ?1 and borrado_logico = ?2 ", nativeQuery = true)
    List<Usuario> buscarPaciente(int rol,int borrado_logico);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update usuario set borrado_logico = ?1 where id_usuario = ?2")
    void borradoLogico(int valor, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update usuario set estado = ?1 where id_usuario = ?2")
    void pasarActivo(String valor, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update usuario set estado = ?1 where id_usuario = ?2")
    void pasarInactivo(String valor, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update usuario set estado_solicitud = ?1 where id_usuario = ?2")
    void aceptarAdministrador(String valor, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update usuario set estado_solicitud = ?1 where id_usuario = ?2")
    void rechazarAdministrador(String valor, int id);

    @Query(value = "select * from usuario where rol = ?1 and borrado_logico = ?2 order by fecha_creacion", nativeQuery = true)
    Usuario buscarSuperadmin(String rol,int borrado_logico);

    @Query(value = "select * from usuario u inner join usuario_has_sede uhs on (u.id_usuario = uhs.usuario_id_usuario) where rol = 'farmacista' and sede_id_sede = ?1", nativeQuery = true)
    List<Usuario> buscarFarmacistaporSede(int sede_id_sede);

    @Query(value = "select * from usuario u inner join usuario_has_sede uhs on (u.id_usuario = uhs.usuario_id_usuario) where rol = 'Doctor' and sede_id_sede = ?1", nativeQuery = true)
    List<Usuario> buscarDoctorporSede(int sede_id_sede);

    @Query(value = "select * from usuario where correo = ?1 ", nativeQuery = true)
    List<Usuario> buscarPorCorreo(String correo);
}
