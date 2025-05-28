package com.klok.desafio.controllers;

import com.klok.desafio.entities.Item;
import com.klok.desafio.exceptions.StandardError;
import com.klok.desafio.services.item.ItemService;
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
@RequestMapping(value = "/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Operation(summary = "Listar todos os itens",
            description = "Retorna uma lista contendo todos os itens cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "400", description = "Nenhum item encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    public ResponseEntity<List<Item>> listarItems() {
        return ResponseEntity.ok(itemService.listarItens());
    }

    @Operation(summary = "Buscar item por ID",
            description = "Busca e retorna um item específico com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado para o ID informado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(itemService.buscarItemPorId(id));
    }

    @Operation(summary = "Salvar um novo item",
            description = "Adiciona um novo item. Por padrão, itens estão associados a pedidos e, geralmente, não são cadastrados isoladamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item salvo com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao salvar item (item nulo ou inválido).",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    public ResponseEntity<Item> salvar(@RequestBody Item item) {
        item = itemService.salvarItem(item);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(item.getId()).toUri();
        return ResponseEntity.created(uri).body(item);
    }

    @Operation(summary = "Excluir item por ID",
            description = "Exclui um item do sistema, garantindo que ele seja removido de pedidos associados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado para o ID informado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        itemService.excluirItem(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar item por ID",
            description = "Atualiza um item específico no sistema com base no ID informado e nos dados enviados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado para o ID informado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Item> atualizar(@PathVariable Long id, @RequestBody Item item) {
        item = itemService.atualizarItem(id, item);
        return ResponseEntity.ok().body(item);
    }
}