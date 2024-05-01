package com.example.webapp.repository;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
