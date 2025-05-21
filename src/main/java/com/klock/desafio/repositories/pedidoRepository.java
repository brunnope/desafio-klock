package com.klock.desafio.repositories;

import com.klock.desafio.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface pedidoRepository extends JpaRepository<Pedido, Long> {
}
