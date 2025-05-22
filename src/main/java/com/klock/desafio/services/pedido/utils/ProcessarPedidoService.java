package com.klock.desafio.services.pedido.utils;

import com.klock.desafio.entities.Item;
import com.klock.desafio.entities.Pedido;
import com.klock.desafio.exceptions.InsufficientStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProcessarPedidoService {

    private final int DIAS_ENTREGA = 3;

    @Autowired
    private CalculadoraPedidoService calculadoraPedidoService;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private PedidoValidator pedidoValidator;

    public void processarPedido(Pedido pedido) {
        calculadoraPedidoService.calcularValorTotalPedido(pedido);
        calculadoraPedidoService.calcularValorTotalComDesconto(pedido);
        verificarEstoque(pedido);
        definirDataEntrega(pedido);
        pedidoValidator.validarPedido(pedido);
        atualizarEstoque(pedido);
    }



    private void verificarEstoque(Pedido pedido){
        if (!estoqueService.verificarEstoque(pedido)){
            throw new InsufficientStockException();
        }
        pedido.setEmEstoque(true);
    }

    private void definirDataEntrega(Pedido pedido) {
        if (pedido.getEmEstoque()){
            pedido.setDataEntrega(LocalDate.now().plusDays(DIAS_ENTREGA));
        } else {
            pedido.setDataEntrega(null);
        }
    }

    private void atualizarEstoque(Pedido pedido) {
        for (Item item : pedido.getItens()) {
            item.setEstoque(item.getEstoque() - item.getQuantidade());
        }
    }

}
