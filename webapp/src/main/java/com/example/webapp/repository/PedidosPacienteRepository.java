package com.example.webapp.repository;


import com.example.webapp.entity.PedidosPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidosPacienteRepository extends JpaRepository<PedidosPaciente, Integer> {
    @Query(value = "select * from pedidos_paciente where tipo_de_pedido = ?1", nativeQuery = true)
    List<PedidosPaciente> buscarPedidosPorTipo(String tipo_de_pedido);
}
