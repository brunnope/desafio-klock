package com.klock.desafio.services.cliente;

import com.klock.desafio.entities.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClienteService {

    Cliente buscarPorId(Long id) throws Exception;

    List<Cliente> listarClientes();

    Cliente salvarCliente(Cliente cliente);

    Cliente atualizarCliente(Long id, Cliente clienteAtualizado);

    void excluirCliente(Long id) throws Exception;

}
