package com.klok.desafio.services.pedido.utils;

import com.klok.desafio.entities.Pedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculadoraPedidoService {

    private final BigDecimal DESCONTO_VIP = BigDecimal.valueOf(0.1);

    public void calcularValorTotalPedido(Pedido pedido) {
        BigDecimal total = pedido.getItens().stream()
                .map(item -> item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setTotal(total.setScale(2, RoundingMode.HALF_UP));
    }

    public void calcularValorTotalComDesconto(Pedido pedido) {
        if (pedido.getCliente() != null && pedido.getCliente().isVip()) {
            BigDecimal total = pedido.getTotal();
            BigDecimal desconto = total.multiply(DESCONTO_VIP);
            pedido.setTotalComDesconto(total.subtract(desconto).setScale(2, RoundingMode.HALF_UP));
        } else {
            pedido.setTotalComDesconto(pedido.getTotal().setScale(2, RoundingMode.HALF_UP));
        }
    }
}
