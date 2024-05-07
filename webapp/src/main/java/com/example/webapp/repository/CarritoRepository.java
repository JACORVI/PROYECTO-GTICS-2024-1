package com.example.webapp.repository;

import com.example.webapp.entity.Carrito;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    @Query(value = "select * from carrito where medicamentos_id_medicamentos=?1", nativeQuery = true)
    List<Carrito> buscarDuplicados(int id);

    @Query(value = "select cantidad \n" +
            "from gticsbd.carrito \n" +
            "where medicamentos_id_medicamentos =?1", nativeQuery = true)
    int cantidadDelDuplicado(int id);

    @Transactional
    @Modifying
    @Query(value = "delete from carrito where (medicamentos_id_medicamentos = ?1) and (`usuario_id_usuario` = ?2);", nativeQuery = true)
    void borrarDuplicado(int id, int usuid);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "INSERT INTO carrito (medicamentos_id_medicamentos, usuario_id_usuario, cantidad)\n" +
            "VALUES \n" +
            "(?1, ?2, ?3)")
    void AñadirAlCarrito(int idMedicamentos, int idUsuario, int cantidad);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from carrito where medicamentos_id_medicamentos= ?1")
    void EvitaDuplicados(int idMedicamentos);
}
