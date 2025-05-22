package com.klock.desafio.services.cliente;

import com.klock.desafio.entities.Cliente;
import com.klock.desafio.exceptions.BusinessRuleException;
import com.klock.desafio.exceptions.DatabaseException;
import com.klock.desafio.exceptions.ResourceNotFoundException;
import com.klock.desafio.repositories.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente buscarPorId(Long id) throws Exception {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw new BusinessRuleException("Nenhum cliente encontrado.");
        }
        return clientes;
    }

    @Override
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente atualizarCliente(Long id, Cliente cliente) {
        try {
            Cliente clienteUpdate = clienteRepository.getReferenceById(id);
            atualizarDados(clienteUpdate, cliente);
            return clienteRepository.save(clienteUpdate);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    @Override
    public void excluirCliente(Long id) throws Exception {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    private void atualizarDados(Cliente clienteUpdate, Cliente cliente) {
        clienteUpdate.setNome(cliente.getNome());
        clienteUpdate.setEmail(cliente.getEmail());
        clienteUpdate.setVip(cliente.isVip());
    }
}
