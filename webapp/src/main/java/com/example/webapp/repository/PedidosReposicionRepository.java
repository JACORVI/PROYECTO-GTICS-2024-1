package com.example.webapp.repository;

import com.example.webapp.dto.MedicamentosPocoInventarioDto;
import com.example.webapp.entity.PedidosReposicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidosReposicionRepository extends JpaRepository<PedidosReposicion, Integer> {
    @Query(value = "SELECT * FROM pedidos_reposicion where usuario_id_usuario = ?1 order by fecha_solicitud desc", nativeQuery = true)
    List<PedidosReposicion> listarPedRepPorIdUsuario(int usuario_id_usuario);
}
