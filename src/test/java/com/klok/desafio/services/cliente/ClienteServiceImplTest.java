package com.klok.desafio.services.cliente;

import com.klok.desafio.entities.Cliente;
import com.klok.desafio.exceptions.BusinessRuleException;
import com.klok.desafio.exceptions.DatabaseException;
import com.klok.desafio.exceptions.ResourceNotFoundException;
import com.klok.desafio.repositories.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void buscarPorId_comSucesso() throws Exception {

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar cliente inexistente por ID")
    void buscarPorId_quandoNaoEncontrar() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorId(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve listar todos os clientes com sucesso")
    void listarClientes_comSucesso() {

        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();

        when(clienteRepository.findAll()).thenReturn(List.of(cliente1, cliente2));

        List<Cliente> clientes = clienteService.listarClientes();

        assertNotNull(clientes);
        assertEquals(2, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando não houver clientes cadastrados")
    void listarClientes_quandoNaoHouverClientes() {

        when(clienteRepository.findAll()).thenReturn(List.of());

        assertThrows(BusinessRuleException.class, () -> clienteService.listarClientes());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve salvar um cliente com sucesso")
    void salvarCliente_comSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.salvarCliente(cliente);

        assertNotNull(resultado);
        assertEquals("Cliente Teste", resultado.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void atualizarCliente_comSucesso() {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setNome("Nome Original");
        clienteExistente.setEmail("<EMAIL>");
        clienteExistente.setVip(false);

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("Nome Atualizado");
        clienteAtualizado.setEmail("<EMAIL_ATUALIZADO>");
        clienteAtualizado.setVip(true);

        when(clienteRepository.getReferenceById(1L)).thenReturn(clienteExistente);
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteExistente);

        Cliente resultado = clienteService.atualizarCliente(1L, clienteAtualizado);

        assertNotNull(resultado);
        assertEquals("Nome Atualizado", resultado.getNome());
        assertEquals("<EMAIL_ATUALIZADO>", resultado.getEmail());
        assertTrue(resultado.isVip());
        verify(clienteRepository, times(1)).getReferenceById(1L);
        verify(clienteRepository, times(1)).save(clienteExistente);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar cliente inexistente")
    void atualizarCliente_quandoNaoEncontrar() {

        Cliente clienteAtualizado = new Cliente();

        when(clienteRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> clienteService.atualizarCliente(1L, clienteAtualizado));

        verify(clienteRepository, times(1)).getReferenceById(1L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve excluir cliente com sucesso")
    void excluirCliente_comSucesso() throws Exception {

        when(clienteRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> clienteService.excluirCliente(1L));

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar excluir cliente inexistente")
    void excluirCliente_quandoNaoEncontrar() {

        when(clienteRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> clienteService.excluirCliente(1L));

        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar DatabaseException ao excluir cliente associado a outros registros")
    void excluirCliente_quandoAssociadoAOutrosRegistros(){

        when(clienteRepository.existsById(1L)).thenReturn(true);

        doThrow(new DataIntegrityViolationException("Erro de integridade referencial")).when(clienteRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> clienteService.excluirCliente(1L));

        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }
}