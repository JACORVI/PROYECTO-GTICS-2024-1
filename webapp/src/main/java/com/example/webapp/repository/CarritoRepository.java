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

    @Query(value = "SELECT gticsbd.carrito.numero_pedido\n" +
            "FROM gticsbd.carrito\n" +
            "WHERE gticsbd.carrito.usuario_id_usuario = ?1", nativeQuery = true)
    List<String> numPedidoPorUsuarioId(int id);

    @Query(value = "SELECT idpedidos_paciente\n" +
            "FROM gticsbd.pedidos_paciente\n" +
            "WHERE estado_del_pedido = 'Registrado' AND usuario_id_usuario = ?", nativeQuery = true)
    List<Integer> idpedidoPorUsuIdDely(int id);

    @Query(value = "SELECT idpedidos_paciente_recojo\n" +
            "FROM gticsbd.pedidos_paciente_recojo\n" +
            "WHERE estado_del_pedido = 'Registrado' AND usuario_id_usuario = ?", nativeQuery = true)
    List<Integer> idpedidoPorUsuIdReco(int id);

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
    void registrarPedidoDely(double costototal, String tipopedido, String validacionpedido, String estadopedido, String numpedido, int usuid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into gticsbd.pedidos_paciente_recojo (costo_total, tipo_de_pedido, validacion_del_pedido, estado_del_pedido, numero_tracking, usuario_id_usuario)\n" +
            "VALUES(?1, ?2, ?3, ?4, ?5, ?6)")
    void registrarPedidoReco(double costototal, String tipopedido, String validacionpedido, String estadopedido, String numpedido, int usuid);

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
    void finalizarPedido1(String nombre, String apellido, int dni, int telefono, String seguro, String medico, String vencimiento, String fechasoli, String direccion, String distrito, String horaentrega, String estadopedido,int usuid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE gticsbd.pedidos_paciente_recojo \n" +
            "SET nombre_paciente = ?1, \n" +
            "    apellido_paciente = ?2, \n" +
            "    dni = ?3,\n" +
            "    telefono = ?4, \n" +
            "    seguro = ?5, \t\n" +
            "    medico_que_atiende = ?6, \n" +
            "    aviso_vencimiento = ?7, \t\n" +
            "    fecha_solicitud = ?8,\n" +
            "    estado_del_pedido = ?9,\n" +
            "    sede_de_recojo = ?10\n" +
            "WHERE estado_del_pedido = 'Registrando' AND usuario_id_usuario = ?11")
    void finalizarPedido2(String nombre, String apellido, int dni, int telefono, String seguro, String medico, String vencimiento, String fechasoli, String estadopedido, String sederecojo, int usuid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM gticsbd.pedidos_paciente_recojo\n" +
            "WHERE usuario_id_usuario = ?1\n" +
            "AND estado_del_pedido = 'Registrando';", nativeQuery = true)
    void cancelarPedidoReco(int usuid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM gticsbd.pedidos_paciente\n" +
            "WHERE usuario_id_usuario = ?1\n" +
            "AND estado_del_pedido = 'Registrando';", nativeQuery = true)
    void cancelarPedidoDely(int usuid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM gticsbd.carrito\n" +
            "WHERE usuario_id_usuario = ?1", nativeQuery = true)
    void borrarCarritoPorId(int usuid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO gticsbd.medicamentos_del_pedido (nombre_medicamento, costo_medicamento, cantidad, pedidos_paciente_idpedidos_paciente, pedidos_paciente_usuario_id_usuario)\n" +
            "SELECT m.nombre AS nombre_medicamento, m.precio_unidad AS costo_medicamento, c.cantidad, ?1, ?2\n" +
            "FROM gticsbd.carrito c\n" +
            "JOIN gticsbd.medicamentos m ON c.medicamentos_id_medicamentos = m.id_medicamentos;", nativeQuery = true)
    void registrarMedicamentosPedidoDely(int idpedido, int usuid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO gticsbd.medicamentos_recojo (nombre_medicamento, costo_medicamento, cantidad, pedidos_paciente_recojo_idpedidos_paciente_recojo, pedidos_paciente_recojo_usuario_id_usuario)\n" +
            "SELECT m.nombre AS nombre_medicamento, m.precio_unidad AS costo_medicamento, c.cantidad, ?1, ?2\n" +
            "FROM gticsbd.carrito c\n" +
            "JOIN gticsbd.medicamentos m ON c.medicamentos_id_medicamentos = m.id_medicamentos;", nativeQuery = true)
    void registrarMedicamentosPedidoReco(int idpedido, int usuid);
}
