package com.klock.desafio.repositories;

import com.klock.desafio.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface clienteRepository extends JpaRepository<Cliente, Long> {
}
