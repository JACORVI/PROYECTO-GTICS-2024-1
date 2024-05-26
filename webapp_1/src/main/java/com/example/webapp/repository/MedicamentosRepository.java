package com.example.webapp.repository;

import com.example.webapp.dto.MedicamentosPocoInventarioDto;
import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Usuario;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
