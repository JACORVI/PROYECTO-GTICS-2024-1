package com.example.webapp.repository;

import com.example.webapp.entity.MedicamentosDelPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentosDelPedidoRepository extends JpaRepository<MedicamentosDelPedido, Integer>{
}
