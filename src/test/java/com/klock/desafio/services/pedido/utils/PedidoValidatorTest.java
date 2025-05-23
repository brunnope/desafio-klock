package com.klock.desafio.services.pedido.utils;

import com.klock.desafio.entities.Cliente;
import com.klock.desafio.entities.Item;
import com.klock.desafio.entities.Pedido;
import com.klock.desafio.exceptions.BusinessRuleException;
import com.klock.desafio.services.item.utils.ItemValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoValidatorTest {

    @Mock
    private ItemValidator itemValidator;

    @InjectMocks
    private PedidoValidator pedidoValidator;

    @Test
    @DisplayName("Deve lançar a exceção para pedido Null")
    void deveLancarExcecao_quandoPedidoForNulo() {

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> pedidoValidator.validarPedido(null));
        assertEquals("Pedido não pode ser nulo.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para cliente Null")
    void deveLancarExcecao_quandoClienteForNulo() {

        Pedido pedido = new Pedido();

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pedidoValidator.validarPedido(pedido));
        assertEquals("Pedido deve estar associado a um cliente.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para cliente de ID Null")
    void deveLancarExcecao_quandoIdDoClienteForNulo() {

        Pedido pedido = new Pedido();
        pedido.setCliente(new Cliente());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pedidoValidator.validarPedido(pedido));
        assertEquals("Cliente associado ao pedido não possui um ID válido.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lança exceção para pedidos sem itens")
    void deveLancarExcecao_quandoPedidoNaoConterItens() {

        Pedido pedido = new Pedido();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        pedido.setCliente(cliente);
        pedido.getItens().addAll(Collections.emptyList());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pedidoValidator.validarPedido(pedido));
        assertEquals("Pedido deve conter pelo menos um item.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar cada item de pedido")
    void deveValidarItensDoPedidoChamandoItemValidator() {

        Pedido pedido = new Pedido();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        pedido.setCliente(cliente);

        Item item1 = new Item();
        Item item2 = new Item();
        pedido.getItens().addAll(List.of(item1, item2));

        pedido.setTotal(BigDecimal.TEN);
        pedido.setTotalComDesconto(BigDecimal.ONE);

        pedidoValidator.validarPedido(pedido);

        verify(itemValidator, times(2)).validarItem(any(Item.class));
    }

    @Test
    @DisplayName("Deve lançar exceção para valor total de pedido inválido")
    void deveLancarExcecao_quandoTotalDoPedidoForInvalido() {

        Pedido pedido = new Pedido();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        pedido.setCliente(cliente);
        pedido.getItens().addAll(List.of(new Item()));
        pedido.setTotal(BigDecimal.ZERO);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pedidoValidator.validarPedido(pedido));
        assertEquals("O total do pedido deve ser maior que zero.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para total com desconto ser negativo")
    void deveLancarExcecao_quandoTotalComDescontoForNegativo() {

        Pedido pedido = new Pedido();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        pedido.setCliente(cliente);
        pedido.getItens().addAll(List.of(new Item()));
        pedido.setTotal(BigDecimal.TEN);
        pedido.setTotalComDesconto(BigDecimal.valueOf(-1));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pedidoValidator.validarPedido(pedido));
        assertEquals("O total com desconto não pode ser negativo.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para datas já passdas")
    void deveLancarExcecao_quandoDataEntregaEstiverNoPassado() {

        Pedido pedido = new Pedido();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        pedido.setCliente(cliente);
        pedido.getItens().addAll(List.of(new Item()));
        pedido.setTotal(BigDecimal.TEN);
        pedido.setTotalComDesconto(BigDecimal.ONE);
        pedido.setDataEntrega(LocalDate.now().minusDays(1));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> pedidoValidator.validarPedido(pedido));
        assertEquals("A data de entrega não pode ser uma data no passado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar pedido com sucesso quando estoque suficiente para todos os itens")
    void deveValidarPedidoComSucesso() {

        Pedido pedido = new Pedido();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        pedido.setCliente(cliente);

        Item item1 = new Item();
        item1.setEstoque(5);
        pedido.getItens().addAll(List.of(item1));

        pedido.setTotal(BigDecimal.TEN);
        pedido.setTotalComDesconto(BigDecimal.valueOf(9));
        pedido.setDataEntrega(LocalDate.now().plusDays(3));

        assertDoesNotThrow(() -> pedidoValidator.validarPedido(pedido));
    }
}