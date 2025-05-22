package com.klock.desafio.services.item;

import com.klock.desafio.entities.Item;
import com.klock.desafio.entities.Pedido;
import com.klock.desafio.exceptions.BusinessRuleException;
import com.klock.desafio.exceptions.ResourceNotFoundException;
import com.klock.desafio.repositories.ItemRepository;
import com.klock.desafio.repositories.PedidoRepository;
import com.klock.desafio.services.item.utils.ItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemValidator itemValidator;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Item> listarItens() {
        List<Item> itens = itemRepository.findAll();
        if (itens.isEmpty()) {
                throw new BusinessRuleException("Nenhum item encontrado.");
        }
        return itens;
    }

    @Override
    public Item buscarItemPorId(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public Item salvarItem(Item item) {
        if (item == null) {
            throw new BusinessRuleException("O item não pode ser nulo.");
        }
        itemValidator.validarItem(item);
        return itemRepository.save(item);
    }

    @Override
    public Item atualizarItem(Long id, Item itemAtualizado) {
        Item itemExistente = itemRepository.getReferenceById(id);

        if (itemAtualizado.getNome() != null) {
            itemExistente.setNome(itemAtualizado.getNome());
        }

        return itemRepository.save(itemExistente);
    }

    @Override
    public void excluirItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Pedido pedido = item.getPedido();
        pedido.getItens().remove(item);
        item.setPedido(null);
        //ao remover um item de um pedido, automaticamente esse item é apagado do banco
        pedidoRepository.save(pedido);
    }

    private void atualizarDados(Item itemExistente, Item itemAtualizado){
        itemExistente.setNome(itemAtualizado.getNome());
        itemExistente.setPreco(itemAtualizado.getPreco());
        itemExistente.setQuantidade(itemAtualizado.getQuantidade());
        itemExistente.setEstoque(itemAtualizado.getEstoque());
        itemExistente.setPedido(itemAtualizado.getPedido());
    }
}