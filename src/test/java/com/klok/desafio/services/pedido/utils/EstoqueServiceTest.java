package com.klok.desafio.services.pedido.utils;

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
class EstoqueServiceTest {

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    @DisplayName("Deve retornar true para estoque suficiente para todos os itens")
    void deveRetornarTrue_quandoEstoqueSuficiente() {

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 2, 10, null);
        Item item2 = new Item("Produto B", BigDecimal.valueOf(30), 1, 5, null);
        Pedido pedido = new Pedido();
        pedido.getItens().addAll(List.of(item1, item2));

        boolean resultado = estoqueService.verificarEstoque(pedido);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar false para estoque insuficiente para algum dos itens")
    void deveRetornarFalse_quandoItemSemEstoqueSuficiente() {

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 2, 1, null); // Estoque insuficiente
        Item item2 = new Item("Produto B", BigDecimal.valueOf(30), 1, 5, null);
        Pedido pedido = new Pedido();
        pedido.getItens().addAll(List.of(item1, item2));

        boolean resultado = estoqueService.verificarEstoque(pedido);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar true para lista sem itens")
    void deveRetornarTrue_quandoListaDeItensVazia() {
        Pedido pedido = new Pedido();
        pedido.getItens().addAll(List.of());

        boolean resultado = estoqueService.verificarEstoque(pedido);

        assertTrue(resultado);
    }
}