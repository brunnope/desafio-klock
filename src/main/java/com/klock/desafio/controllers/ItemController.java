package com.klock.desafio.controllers;

import com.klock.desafio.entities.Item;
import com.klock.desafio.services.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value="/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<Item>> listarItems() {
        return ResponseEntity.ok(itemService.listarItens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(@PathVariable Long id) throws Exception {

        return ResponseEntity.ok().body(itemService.buscarItemPorId(id));
    }

    @PostMapping
    public ResponseEntity<Item> salvar(@RequestBody Item pedido){
        pedido = itemService.salvarItem(pedido);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) throws Exception {
        itemService.excluirItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> atualizar(@PathVariable Long id, @RequestBody Item pedido){
        pedido = itemService.atualizarItem(id, pedido);
        return ResponseEntity.ok().body(pedido);
    }
}
