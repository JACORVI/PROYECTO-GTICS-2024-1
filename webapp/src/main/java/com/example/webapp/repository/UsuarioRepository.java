package com.example.webapp.repository;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByRol(String textoIngresado);

    @Query(value = "select * from usuario where rol = ?1 and borrado_logico = ?2 order by fecha_creacion", nativeQuery = true)
    List<Usuario> buscarFarmacista(String rol,int borrado_logico);

    @Query(value = "select * from usuario where rol = ?1 and borrado_logico = ?2 ", nativeQuery = true)
    List<Usuario> buscarDoctor(String rol,int borrado_logico);

    @Query(value = "select * from usuario where rol = ?1 and borrado_logico = ?2 ", nativeQuery = true)
    List<Usuario> buscarAdministrador(String rol,int borrado_logico);

    @Query(value = "select * from usuario where rol = ?1 and borrado_logico = ?2 ", nativeQuery = true)
    List<Usuario> buscarPaciente(String rol,int borrado_logico);

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
}
