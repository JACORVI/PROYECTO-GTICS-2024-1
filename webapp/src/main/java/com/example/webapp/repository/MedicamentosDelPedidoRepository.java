package com.example.webapp.repository;

import com.example.webapp.entity.MedicamentosDelPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentosDelPedidoRepository extends JpaRepository<MedicamentosDelPedido, Integer>{
    @Query(value = "SELECT * \n" +
            "FROM gticsbd.medicamentos_del_pedido \n" +
            "WHERE pedidos_paciente_idpedidos_paciente = ?1", nativeQuery = true)
    List<MedicamentosDelPedido> listaMedicamentosDely(int idpedido);
}
