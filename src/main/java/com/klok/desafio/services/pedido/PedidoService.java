package com.klok.desafio.services.pedido;

import com.klok.desafio.entities.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido buscarPorId(Long id);

    List<Pedido> listarPedidos();

    Pedido salvarPedido(Pedido pedido);

    Pedido atualizarPedido(Long id, Pedido pedido);

    void excluirPedido(Long id);
}