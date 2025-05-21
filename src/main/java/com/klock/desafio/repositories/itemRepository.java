package com.klock.desafio.repositories;

import com.klock.desafio.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface itemRepository extends JpaRepository<Item, Long> {
}
