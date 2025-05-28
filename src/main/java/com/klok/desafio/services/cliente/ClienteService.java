package com.klok.desafio.services.cliente;

import com.klok.desafio.entities.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente buscarPorId(Long id) throws Exception;

    List<Cliente> listarClientes();

    Cliente salvarCliente(Cliente cliente);

    Cliente atualizarCliente(Long id, Cliente clienteAtualizado);

    void excluirCliente(Long id) throws Exception;

}
