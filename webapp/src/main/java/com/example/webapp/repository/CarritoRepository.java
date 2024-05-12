package com.example.webapp.repository;

import com.example.webapp.entity.Carrito;
import com.example.webapp.entity.CarritoId;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, CarritoId> {

    @Query(value = "SELECT *\n" +
            "FROM gticsbd.carrito\n" +
            "WHERE estado_de_compra != 'Registrado';", nativeQuery = true)
    List<Carrito> listarCarrito();

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

    @Query(value = "SELECT gticsbd.pedidos_paciente.tipo_de_pedido \n" +
            "FROM gticsbd.pedidos_paciente \n" +
            "WHERE gticsbd.pedidos_paciente.usuario_id_usuario = ?1\n" +
            "AND gticsbd.pedidos_paciente.estado_del_pedido = ?2", nativeQuery = true)
    List<String> tipoDePedidoPorUsuarioId(int id, String estadopedido);

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

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into gticsbd.pedidos_paciente (costo_total, tipo_de_pedido, validacion_del_pedido, estado_del_pedido, numero_tracking, usuario_id_usuario)\n" +
            "VALUES(?1, ?2, ?3, ?4, ?5, ?6)")
    void registrarPedido(double costototal, String tipopedido, String validacionpedido, String estadopedido, String numpedido, int usuid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE gticsbd.pedidos_paciente \n" +
            "SET nombre_paciente = ?1, \n" +
            "    apellido_paciente = ?2, \n" +
            "    dni = ?3,\n" +
            "    telefono = ?4, \n" +
            "    seguro = ?5, \t\n" +
            "    medico_que_atiende = ?6, \n" +
            "    aviso_vencimiento = ?7, \t\n" +
            "    fecha_solicitud = ?8,\n" +
            "    direccion = ?9, \n" +
            "    distrito = ?10, \n" +
            "    hora_de_entrega = ?11,\n" +
            "    estado_del_pedido = ?12\n" +
            "WHERE estado_del_pedido = 'Registrando' AND usuario_id_usuario = ?13")
    void finalizarPedido(String nombre, String apellido, int dni, int telefono, String seguro, String medico, String vencimiento, String fechasoli, String direccion, String distrito, String horaentrega, String estadopedido,int usuid);
}
