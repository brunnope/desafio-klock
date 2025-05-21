package com.klock.desafio.services.cliente;

import com.klock.desafio.entities.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Override
    public Cliente buscarPorId(Long id) {
        return null;
    }

    @Override
    public List<Cliente> listarClientes() {
        return List.of();
    }

    @Override
    public Cliente salvarCliente(Cliente cliente) {
        return null;
    }

    @Override
    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        return null;
    }

    @Override
    public void excluirCliente(Long id) {

    }
}
