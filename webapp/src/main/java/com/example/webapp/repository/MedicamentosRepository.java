package com.example.webapp.repository;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentosRepository extends JpaRepository<Medicamentos, Integer> {
    List<Medicamentos> findByNombre(String textoIngresado);
    @Query(value = "select * from medicamentos where nombre like %?1%" , nativeQuery = true)
    List<Medicamentos> buscarMedicamento(String nombreMedicamento);
}
