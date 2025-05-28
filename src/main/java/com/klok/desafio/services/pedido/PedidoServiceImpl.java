package com.klok.desafio.services.pedido;

import com.klok.desafio.entities.Item;
import com.klok.desafio.entities.Pedido;
import com.klok.desafio.exceptions.BusinessRuleException;
import com.klok.desafio.exceptions.DatabaseException;
import com.klok.desafio.exceptions.ResourceNotFoundException;
import com.klok.desafio.repositories.PedidoRepository;
import com.klok.desafio.services.pedido.utils.ProcessarPedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProcessarPedidoService processarPedidoService;

    @Override
    public Pedido buscarPorId(Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        if (pedidos.isEmpty()) {
            throw new BusinessRuleException("Nenhum pedido encontrado.");
        }
        return pedidos;
    }

    @Override
    public Pedido salvarPedido(Pedido pedido) {
        prepararPedidoComItens(pedido);
        processarPedidoService.processarPedido(pedido);
        return pedidoRepository.save(pedido);
    }


    @Override
    public Pedido atualizarPedido(Long id, Pedido pedidoAtualizado) {
        try {
            Pedido pedidoExistente = pedidoRepository.getReferenceById(id);
            atualizarDados(pedidoExistente, pedidoAtualizado);
            prepararPedidoComItens(pedidoExistente);
            processarPedidoService.processarPedido(pedidoExistente);
            return pedidoRepository.save(pedidoExistente);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }


    @Override
    public void excluirPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        try {
            pedidoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao excluir pedido. Ele pode estar associado a outros registros.");
        }
    }

    private void prepararPedidoComItens(Pedido pedido) {
        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> item.setPedido(pedido));
        }
    }



    private void atualizarDados(Pedido pedidoExistente, Pedido pedidoAtualizado) {
        pedidoExistente.setTotal(pedidoAtualizado.getTotal());
        pedidoExistente.setTotalComDesconto(pedidoAtualizado.getTotalComDesconto());
        pedidoExistente.setEmEstoque(pedidoAtualizado.getEmEstoque());
        pedidoExistente.setDataEntrega(pedidoAtualizado.getDataEntrega());
        pedidoExistente.setCliente(pedidoAtualizado.getCliente());

        pedidoExistente.getItens().clear();
        for (Item item : pedidoAtualizado.getItens()) {
            item.setPedido(pedidoExistente);
            pedidoExistente.getItens().add(item);
        }
    }
}