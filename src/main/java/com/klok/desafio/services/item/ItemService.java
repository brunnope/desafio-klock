package com.klok.desafio.services.item;

import com.klok.desafio.entities.Item;

import java.util.List;

public interface ItemService {

    List<Item> listarItens();

    Item buscarItemPorId(Long id);

    Item salvarItem(Item item);

    Item atualizarItem(Long id, Item item);

    void excluirItem(Long id);
}