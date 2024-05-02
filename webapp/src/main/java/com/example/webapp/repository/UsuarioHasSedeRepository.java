package com.example.webapp.repository;

import com.example.webapp.entity.Usuario;
import com.example.webapp.entity.UsuarioHasSede;
import com.example.webapp.entity.UsuarioHasSedeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioHasSedeRepository extends JpaRepository<UsuarioHasSede, UsuarioHasSedeId> {

    @Query(value = "select * from usuario_has_sede where usuario_id_usuario = ?1", nativeQuery = true)
    List<UsuarioHasSede> buscarSede(int id);


}
