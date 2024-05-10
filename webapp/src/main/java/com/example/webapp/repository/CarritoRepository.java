package com.example.webapp.repository;

import com.example.webapp.entity.Carrito;
import com.example.webapp.entity.CarritoId;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, CarritoId> {

    @Query(value = "select * from carrito where medicamentos_id_medicamentos=?1", nativeQuery = true)
    List<Carrito> buscarDuplicados(int id);

    @Query(value = "select cantidad\n" +
            "from gticsbd.carrito\n" +
            "where medicamentos_id_medicamentos = ?1", nativeQuery = true)
    int cantidadDelDuplicado(int id);

    @Transactional
    @Modifying
    @Query(value = "delete from carrito where (medicamentos_id_medicamentos = ?1) and (`usuario_id_usuario` = ?2);", nativeQuery = true)
    void borrarElementoCarrito(int id, int usuid);

    @Transactional
    @Modifying
    @Query(value = "delete from gticsbd.carrito where (gticsbd.carrito.usuario_id_usuario = ?1) and (gticsbd.carrito.estado_de_compra = ?2);", nativeQuery = true)
    void borrarPedidoRegistrado(int usuid, String estadocompra);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "INSERT INTO gticsbd.carrito (medicamentos_id_medicamentos, usuario_id_usuario, cantidad, numero_pedido, estado_de_compra)\n" +
            "VALUES(?1, ?2, ?3, ?4, ?5)")
    void AnadirAlCarrito(int idMedicamentos, int idUsuario, int cantidad, String numpedido, String estadocompra);

    @Query(value = "SELECT gticsbd.carrito.estado_de_compra\n" +
            "FROM gticsbd.usuario\n" +
            "JOIN gticsbd.carrito ON gticsbd.usuario.id_usuario = gticsbd.carrito.usuario_id_usuario\n" +
            "WHERE usuario.id_usuario = ?1", nativeQuery = true)
    List<String> estadosDeCompraPorUsuarioId(int id);

    @Query(value = "SELECT gticsbd.carrito.numero_pedido\n" +
            "FROM gticsbd.carrito\n" +
            "WHERE gticsbd.carrito.usuario_id_usuario = ?1", nativeQuery = true)
    List<String> numPedidoPorUsuarioId(int id);

    @Transactional
    @Modifying
    @Query(value = "SELECT gticsbd.carrito.cantidad * gticsbd.medicamentos.precio_unidad AS total\n" +
            "FROM gticsbd.carrito\n" +
            "JOIN \n" +
            "    gticsbd.medicamentos ON gticsbd.carrito.medicamentos_id_medicamentos = gticsbd.medicamentos.id_medicamentos;", nativeQuery = true)
    List<Double> CantidadxPrecioUnitario();
}
