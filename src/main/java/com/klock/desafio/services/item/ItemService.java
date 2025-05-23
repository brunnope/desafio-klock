package com.klock.desafio.services.item;

import com.klock.desafio.entities.Item;

import java.util.List;

public interface ItemService {

    List<Item> listarItens();

    Item buscarItemPorId(Long id);

    Item salvarItem(Item item);

    Item atualizarItem(Long id, Item item);

    void excluirItem(Long id);
}