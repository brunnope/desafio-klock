package com.klok.desafio.controllers;

import com.klok.desafio.entities.Pedido;
import com.klok.desafio.exceptions.StandardError;
import com.klok.desafio.services.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Listar todos os pedidos",
            description = "Retorna uma lista contendo todos os pedidos cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "400", description = "Nenhum pedido encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping()
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @Operation(summary = "Buscar pedido por ID",
            description = "Busca e retorna um pedido específico com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado para o ID informado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(pedidoService.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar um novo pedido",
            description = "Adiciona um novo pedido ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar pedido (valores incorretos ou malformados).",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    public ResponseEntity<Pedido> salvar(@RequestBody Pedido pedido) {
        pedido = pedidoService.salvarPedido(pedido);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(pedido);
    }

    @Operation(summary = "Excluir pedido por ID",
            description = "Exclui um pedido do sistema com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado para o ID informado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir pedido devido a restrições no banco de dados.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidoService.excluirPedido(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar um pedido",
            description = "Atualiza os dados de um pedido com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado para o ID informado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação ao atualizar o pedido.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        pedido = pedidoService.atualizarPedido(id, pedido);
        return ResponseEntity.ok().body(pedido);
    }
}