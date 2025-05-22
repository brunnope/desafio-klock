package com.klock.desafio.controllers;

import com.klock.desafio.entities.Cliente;
import com.klock.desafio.services.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value="/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) throws Exception {

        return ResponseEntity.ok().body(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente){
        cliente = clienteService.salvarCliente(cliente);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).body(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) throws Exception {
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente){
        cliente = clienteService.atualizarCliente(id, cliente);
        return ResponseEntity.ok().body(cliente);
    }
}
