package com.example.webapp.repository;

import com.example.webapp.entity.PedidosPacienteRecojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosPacienteRecojoRepository extends JpaRepository<PedidosPacienteRecojo, Integer> {
}
