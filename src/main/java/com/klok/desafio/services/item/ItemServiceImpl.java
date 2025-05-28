package com.klok.desafio.services.item;

import com.klok.desafio.entities.Item;
import com.klok.desafio.entities.Pedido;
import com.klok.desafio.exceptions.BusinessRuleException;
import com.klok.desafio.exceptions.ResourceNotFoundException;
import com.klok.desafio.repositories.ItemRepository;
import com.klok.desafio.services.item.utils.ItemValidator;
import com.klok.desafio.services.pedido.PedidoService;
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
    private PedidoService pedidoService;

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

    //Implementado mesmo que não se salva item isolado e sim só através de pedido
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
        pedidoService.salvarPedido(pedido);
    }

    private void atualizarDados(Item itemExistente, Item itemAtualizado){
        itemExistente.setNome(itemAtualizado.getNome());
        itemExistente.setPreco(itemAtualizado.getPreco());
        itemExistente.setQuantidade(itemAtualizado.getQuantidade());
        itemExistente.setEstoque(itemAtualizado.getEstoque());
        itemExistente.setPedido(itemAtualizado.getPedido());
    }
}