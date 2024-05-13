package com.example.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosPacienteRecojoRepository extends JpaRepository<PedidosPacienteRecojo, Integer> {
}
