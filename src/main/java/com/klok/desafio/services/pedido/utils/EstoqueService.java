package com.klok.desafio.services.pedido.utils;

import com.klok.desafio.entities.Pedido;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    public boolean verificarEstoque(Pedido pedido) {
        return pedido.getItens().stream()
                .allMatch(item -> item.getQuantidade() <= item.getEstoque());
    }
}
