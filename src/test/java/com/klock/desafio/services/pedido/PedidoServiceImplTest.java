package com.klock.desafio.services.pedido;

import com.klock.desafio.entities.Cliente;
import com.klock.desafio.entities.Item;
import com.klock.desafio.entities.Pedido;
import com.klock.desafio.exceptions.BusinessRuleException;
import com.klock.desafio.exceptions.DatabaseException;
import com.klock.desafio.exceptions.ResourceNotFoundException;
import com.klock.desafio.repositories.PedidoRepository;
import com.klock.desafio.services.pedido.utils.ProcessarPedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProcessarPedidoService processarPedidoService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    @DisplayName("Deve buscar pedido por ID com sucesso")
    void buscarPorId_comSucesso() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar por ID inexistente")
    void buscarPorId_quandoNaoEcontrarPedido() {

        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pedidoService.buscarPorId(1L));
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar todos os pedidos com sucesso")
    void listarPedidos_comSucesso() {

        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

        List<Pedido> pedidos = pedidoService.listarPedidos();

        assertNotNull(pedidos);
        assertEquals(2, pedidos.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando não houver pedidos cadastrados")
    void listarPedidos_quandoNaoHouverPedidos() {

        when(pedidoRepository.findAll()).thenReturn(List.of());

        assertThrows(BusinessRuleException.class, () -> pedidoService.listarPedidos());

        verify(pedidoRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Deve salvar pedido com sucesso")
    void salvarPedido_comSucesso() {

        Pedido pedido = new Pedido();

        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido pedidoSalvo = pedidoService.salvarPedido(pedido);

        assertNotNull(pedidoSalvo);
        verify(pedidoRepository, times(1)).save(pedido);
        verify(processarPedidoService, times(1)).processarPedido(pedido);
    }

    @Test
    @DisplayName("Deve atualizar pedido com sucesso")
    void atualizarPedido_comSucesso() {

        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setTotal(BigDecimal.valueOf(100));
        Cliente cliente = new Cliente("Cliente original", "original@gmail.com");
        pedidoExistente.setCliente(cliente);

        Pedido pedidoAtualizado = new Pedido();
        pedidoAtualizado.setTotal(BigDecimal.valueOf(200));
        Cliente clienteAtualizado = new Cliente("Cliente atualizado", "atualizado@gmail.com");
        pedidoAtualizado.setCliente(clienteAtualizado);

        when(pedidoRepository.getReferenceById(1L)).thenReturn(pedidoExistente);
        when(pedidoRepository.save(pedidoExistente)).thenReturn(pedidoExistente);

        Pedido resultado = pedidoService.atualizarPedido(1L, pedidoAtualizado);

        assertEquals(BigDecimal.valueOf(200), resultado.getTotal());
        assertEquals("Cliente atualizado", resultado.getCliente().getNome());
        verify(pedidoRepository, times(1)).getReferenceById(1L);
        verify(pedidoRepository, times(1)).save(pedidoExistente);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando tentar atualizar pedido que não existe")
    void atualizarPedido_quandoPedidoNaoExistir() {

        when(pedidoRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> pedidoService.atualizarPedido(1L, new Pedido()));

        verify(pedidoRepository, times(1)).getReferenceById(1L);
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve preparar itens corretamente ao atualizar pedido")
    void prepararItens_deveAssociarPedidoAosItens() {

        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId(1L);

        Item item1 = new Item();
        Item item2 = new Item();
        pedidoExistente.getItens().addAll(List.of(item1, item2));

        Pedido pedidoAtualizado = new Pedido();
        pedidoAtualizado.getItens().addAll(List.of(item1, item2));

        when(pedidoRepository.getReferenceById(1L)).thenReturn(pedidoExistente);
        when(pedidoRepository.save(pedidoExistente)).thenReturn(pedidoExistente);

        Pedido resultado = pedidoService.atualizarPedido(1L, pedidoAtualizado);

        assertEquals(pedidoExistente, item1.getPedido(), "O item 1 não foi associado ao pedido existente");
        assertEquals(pedidoExistente, item2.getPedido(), "O item 2 não foi associado ao pedido existente");
        verify(pedidoRepository, times(1)).getReferenceById(1L);
        verify(pedidoRepository, times(1)).save(pedidoExistente);
    }

    @Test
    @DisplayName("Deve excluir pedido com sucesso")
    void excluirPedido_comSucesso() {

        when(pedidoRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> pedidoService.excluirPedido(1L));

        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao excluir pedido inexistente")
    void excluirPedido_QuandoNaoEncontrar() {

        when(pedidoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> pedidoService.excluirPedido(1L));

        verify(pedidoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar DatabaseException ao excluir pedido com violação de integridade")
    void excluirPedido_ComViolacaoDeIntegridade() {

        when(pedidoRepository.existsById(1L)).thenReturn(true);

        doThrow(new DataIntegrityViolationException("")).when(pedidoRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> pedidoService.excluirPedido(1L));

        verify(pedidoRepository, times(1)).existsById(1L);
        verify(pedidoRepository, times(1)).deleteById(1L);

    }
}