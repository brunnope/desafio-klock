package com.klock.desafio.services.pedido.utils;

import com.klock.desafio.entities.Pedido;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    public boolean verificarEstoque(Pedido pedido) {
        return pedido.getItens().stream()
                .allMatch(item -> item.getQuantidade() <= item.getEstoque());
    }
}
