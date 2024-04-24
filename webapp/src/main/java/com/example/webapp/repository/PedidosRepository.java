package com.example.webapp.repository;


import com.example.webapp.entity.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {
    List<Pedidos> findByTipo(String tipo);
}
