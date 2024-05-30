package com.example.webapp.repository;

import com.example.webapp.dto.MedicamentoSolicitados3mesesDto;
import com.example.webapp.dto.MedicamentoSolicitadosxdiasDto;
import com.example.webapp.dto.MedicamentosPocoInventarioDto;
import com.example.webapp.entity.Medicamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicamentosRepository extends JpaRepository<Medicamentos, Integer> {
    List<Medicamentos> findByNombre(String textoIngresado);
    @Query(value = "select * from medicamentos where nombre like %?1%" , nativeQuery = true)
    List<Medicamentos> buscarMedicamento(String nombreMedicamento);

    @Query(value = "select * from medicamentos where borrado_logico = ?1", nativeQuery = true)
    List<Medicamentos> buscarMedicamentoGeneral(int valor);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update medicamentos set borrado_logico = ?1 where id_medicamentos = ?2")
    void borradoLogico(int valor, int id);

    @Query(value = "SELECT id_medicamentos FROM medicamentos ORDER BY id_medicamentos DESC LIMIT 1;\n", nativeQuery = true)
    int buscarUltimo();

    @Query(value = "select * from sede_has_medicamentos shm inner join medicamentos m on (shm.medicamentos_id_medicamentos = m.id_medicamentos) where sede_id_sede = ?1", nativeQuery = true)
    List<Medicamentos> listarMedicamentosporSede(int sede_id_sede);

    @Query(value = "select nombre as Medicamento, inventario as Inventario from medicamentos m inner join sede_has_medicamentos shm  on (m.id_medicamentos = shm.medicamentos_id_medicamentos) where sede_id_sede = ?1 and (inventario < 25)", nativeQuery = true)
    List<MedicamentosPocoInventarioDto> medicamentosConPocoInventario(int sede_id_sede);

    @Query(value = "SELECT m.nombre as Medicamento, count(m.nombre) as CantidadSolicitada FROM medicamentos m inner join pedidos_reposicion_has_medicamentos prhm on (m.id_medicamentos = prhm.medicamentos_id_medicamentos) \n" +
            "inner join pedidos_reposicion pr on (prhm.pedidos_reposicion_id_pedidos_reposicion = pr.id_pedidos_reposicion) where pr.fecha_solicitud >= date_sub(curdate(), INTERVAL ?1 DAY) \n" +
            "group by m.nombre order by cantidadSolicitada desc limit 3;", nativeQuery = true)
    List<MedicamentoSolicitadosxdiasDto> medicamentosSolicitadosxdias(int cantDias);

    @Query(value = "SELECT m.nombre as Medicamento, count(m.nombre) as CantidadSolicitada FROM medicamentos m inner join pedidos_reposicion_has_medicamentos prhm on (m.id_medicamentos = prhm.medicamentos_id_medicamentos) \n" +
            "inner join pedidos_reposicion pr on (prhm.pedidos_reposicion_id_pedidos_reposicion = pr.id_pedidos_reposicion) where pr.fecha_solicitud >= date_sub(curdate(), INTERVAL 3 MONTH) \n" +
            "group by m.nombre order by cantidadSolicitada desc limit 3;", nativeQuery = true)
    List<MedicamentoSolicitados3mesesDto> medicamentosSolicitados3meses();

    @Query(value = "select * from sede_has_medicamentos shm inner join medicamentos m on (shm.medicamentos_id_medicamentos = m.id_medicamentos) where sede_id_sede = ?1 and (inventario < 25)", nativeQuery = true)
    List<Medicamentos> listarMedicamentosConPocoInvporSede(int sede_id_sede);
}
