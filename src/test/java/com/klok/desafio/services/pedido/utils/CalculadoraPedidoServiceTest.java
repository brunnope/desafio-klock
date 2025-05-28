package com.klok.desafio.services.pedido.utils;

import com.klok.desafio.entities.Cliente;
import com.klok.desafio.entities.Item;
import com.klok.desafio.entities.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalculadoraPedidoServiceTest {

    @InjectMocks
    private CalculadoraPedidoService calculadoraPedidoService;

    @Test
    @DisplayName("Deve calcular corretamente com dois itens")
    void deveCalcularCorretamenteValorTotalPedidoComDoisItens() {
        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 2, 10, null);
        Item item2 = new Item("Produto B", BigDecimal.valueOf(30), 1, 5, null);
        Pedido pedido = new Pedido();
        pedido.getItens().addAll(List.of(item1, item2));

        calculadoraPedidoService.calcularValorTotalPedido(pedido);

        assertEquals(new BigDecimal(130).setScale(2), pedido.getTotal());
    }

    @Test
    @DisplayName("Deve retornar zero no valor total para pedidos sem itens")
    void deveRetornarValorTotalZero_quandoPedidoNaoTemItens() {
        Pedido pedido = new Pedido();

        calculadoraPedidoService.calcularValorTotalPedido(pedido);

        assertEquals(BigDecimal.ZERO.setScale(2), pedido.getTotal());
    }


    @Test
    @DisplayName("Deve calcular corretamente o valor total com desconto para clientes vips")
    void deveAplicarDesconto_quandoClienteEhVip() {
        Cliente cliente = new Cliente();
        cliente.setVip(true);
        Pedido pedido = new Pedido(BigDecimal.valueOf(100), BigDecimal.valueOf(0), false, null, cliente);
        calculadoraPedidoService.calcularValorTotalComDesconto(pedido);
        assertEquals(new BigDecimal(90).setScale(2), pedido.getTotalComDesconto());

    }

    @Test
    @DisplayName("Deve deixar o valor com desconto igual de valor total para clientes não vips")
    void naoDeveAplicarDesconto_quandoClienteNaoEhVip() {
        Cliente cliente = new Cliente();
        cliente.setVip(false);
        Pedido pedido = new Pedido(BigDecimal.valueOf(100), BigDecimal.valueOf(0), false, null, cliente);
        calculadoraPedidoService.calcularValorTotalComDesconto(pedido);
        assertEquals(new BigDecimal(100).setScale(2), pedido.getTotalComDesconto());

    }
}