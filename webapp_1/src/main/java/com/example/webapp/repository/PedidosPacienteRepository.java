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

    @Query(value = "SELECT * \n" +
            "FROM gticsbd.pedidos_paciente \n" +
            "WHERE usuario_id_usuario = ?1 AND estado_del_pedido != 'Registrando';", nativeQuery = true)
    List<String> pedidosDelivery(int usuid);

    @Query(value = "SELECT * \n" +
            "FROM gticsbd.pedidos_paciente_recojo \n" +
            "WHERE usuario_id_usuario = ?1 AND estado_del_pedido != 'Registrando';", nativeQuery = true)
    List<String> pedidosRecojo(int usuid);

    @Query(value = "SELECT * \n" +
            "FROM gticsbd.pedidos_paciente \n" +
            "WHERE usuario_id_usuario = ?1 AND estado_del_pedido != 'Registrando' AND tipo_de_pedido = 'Pre-orden';", nativeQuery = true)
    List<String> pedidosPreorden(int usuid);

}
