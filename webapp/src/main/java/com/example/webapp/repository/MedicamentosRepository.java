package com.example.webapp.repository;

import com.example.webapp.entity.Medicamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentosRepository extends JpaRepository<Medicamentos, Integer> {
}
