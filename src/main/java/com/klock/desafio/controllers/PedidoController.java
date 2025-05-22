package com.klock.desafio.controllers;

import com.klock.desafio.entities.Pedido;
import com.klock.desafio.services.pedido.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) throws Exception {

        return ResponseEntity.ok().body(pedidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> salvar(@RequestBody Pedido pedido){
        pedido = pedidoService.salvarPedido(pedido);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) throws Exception {
        pedidoService.excluirPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedido){
        pedido = pedidoService.atualizarPedido(id, pedido);
        return ResponseEntity.ok().body(pedido);
    }
}
