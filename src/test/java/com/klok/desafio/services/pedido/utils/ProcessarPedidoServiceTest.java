package com.klok.desafio.services.pedido.utils;

import com.klok.desafio.entities.Cliente;
import com.klok.desafio.entities.Item;
import com.klok.desafio.entities.Pedido;
import com.klok.desafio.exceptions.InsufficientStockException;
import com.klok.desafio.exceptions.NotificacaoException;
import com.klok.desafio.services.notificacao.Mensagem;
import com.klok.desafio.services.notificacao.NotificacaoService;
import org.apache.commons.mail.EmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarPedidoServiceTest {

    @InjectMocks
    private ProcessarPedidoService processarPedidoService;

    @Mock
    private CalculadoraPedidoService calculadoraPedidoService;

    @Mock
    private EstoqueService estoqueService;

    @Mock
    private PedidoValidator pedidoValidator;

    @Mock
    private NotificacaoService notificacaoService;

    @Test
    @DisplayName("Deve processar todo o pedido com sucesso")
    void deveProcessarPedidoComSucesso() throws EmailException {

        Cliente cliente = new Cliente();
        cliente.setEmail("teste@email.com");

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 2, 10, null);
        Pedido pedido = new Pedido(BigDecimal.valueOf(0), BigDecimal.valueOf(0), false, null, cliente);
        pedido.getItens().add(item1);

        when(estoqueService.verificarEstoque(pedido)).thenReturn(true);

        assertDoesNotThrow(() -> processarPedidoService.processarPedido(pedido));

        verify(calculadoraPedidoService, times(1)).calcularValorTotalPedido(pedido);
        verify(calculadoraPedidoService, times(1)).calcularValorTotalComDesconto(pedido);
        verify(estoqueService, times(1)).verificarEstoque(pedido);
        verify(pedidoValidator, times(1)).validarPedido(pedido);
        verify(notificacaoService, times(1)).enviarEmail(any(Mensagem.class));

    }

    @Test
    @DisplayName("Deve lançar exceção para casos de estoque insuficiente")
    void deveLancarExcecao_quandoEstoqueInsuficiente() throws EmailException {

        Cliente cliente = new Cliente();
        cliente.setEmail("teste@email.com");

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 2, 1, null);
        Pedido pedido = new Pedido(BigDecimal.valueOf(0), BigDecimal.valueOf(0), false, null, cliente);
        pedido.getItens().add((item1));

        when(estoqueService.verificarEstoque(pedido)).thenReturn(false);

        assertThrows(InsufficientStockException.class, () -> processarPedidoService.processarPedido(pedido));
        verify(estoqueService, times(1)).verificarEstoque(pedido);
        verify(notificacaoService, never()).enviarEmail(any());
    }

    @Test
    @DisplayName("Deve definir a data de entrega corretamente")
    void deveDefinirDataEntrega_quandoPedidoEmEstoque() {
        Cliente cliente = new Cliente();
        cliente.setEmail("teste@email.com");

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 1, 10, null);
        Pedido pedido = new Pedido(BigDecimal.valueOf(0), BigDecimal.valueOf(0), true, null, cliente);
        pedido.getItens().add((item1));

        when(estoqueService.verificarEstoque(pedido)).thenReturn(true);

        assertDoesNotThrow(() -> processarPedidoService.processarPedido(pedido));

        assertNotNull(pedido.getDataEntrega());
        assertEquals(LocalDate.now().plusDays(3), pedido.getDataEntrega());
    }

    @Test
    @DisplayName("Deve definir a data como null para pedidos sem estoque")
    void naoDeveDefinirDataEntrega_quandoPedidoNaoEstiverEmEstoque() {

        Cliente cliente = new Cliente();
        cliente.setEmail("teste@email.com");

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 1, 10, null);
        Pedido pedido = new Pedido(BigDecimal.valueOf(0), BigDecimal.valueOf(0), true, null, cliente);
        pedido.getItens().add((item1));

        when(estoqueService.verificarEstoque(pedido)).thenReturn(false);

        assertThrows(InsufficientStockException.class,() -> processarPedidoService.processarPedido(pedido));

        assertNull(pedido.getDataEntrega());
    }

    @Test
    @DisplayName("Deve atualizar o estoque dos itens corretamente")
    void deveAtualizarEstoqueDosItens() {

        Cliente cliente = new Cliente();
        cliente.setEmail("teste@email.com");

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 2, 10, null);
        Item item2 = new Item("Produto B", BigDecimal.valueOf(30), 1, 5, null);
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.getItens().addAll(List.of(item1, item2));

        when(estoqueService.verificarEstoque(pedido)).thenReturn(true);

        processarPedidoService.processarPedido(pedido);

        assertEquals(8, item1.getEstoque());
        assertEquals(4, item2.getEstoque());
    }

    @Test
    @DisplayName("Deve enviar o email para o cliente corretamente")
    void deveEnviarEmailComSucesso() throws EmailException {
        Cliente cliente = new Cliente();
        cliente.setEmail("teste@email.com");

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 1, 10, null);
        Pedido pedido = new Pedido(BigDecimal.valueOf(0), BigDecimal.valueOf(0), true, null, cliente);
        pedido.getItens().add((item1));

        when(estoqueService.verificarEstoque(pedido)).thenReturn(true);

        assertDoesNotThrow(() -> processarPedidoService.processarPedido(pedido));

        verify(notificacaoService, times(1)).enviarEmail(any(Mensagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando falhar o envio de email para cliente")
    void deveLancarNotificacaoException_quandoFalharEnvioEmail() throws EmailException {

        Cliente cliente = new Cliente();
        cliente.setEmail("teste@email.com");

        Item item1 = new Item("Produto A", BigDecimal.valueOf(50), 1, 10, null);
        Pedido pedido = new Pedido(BigDecimal.valueOf(0), BigDecimal.valueOf(0), true, null, cliente);
        pedido.getItens().add((item1));

        when(estoqueService.verificarEstoque(pedido)).thenReturn(true);

        doThrow(RuntimeException.class).when(notificacaoService).enviarEmail(any(Mensagem.class));

        assertThrows(NotificacaoException.class, () -> processarPedidoService.processarPedido(pedido));
        verify(notificacaoService, times(1)).enviarEmail(any(Mensagem.class));
    }
}