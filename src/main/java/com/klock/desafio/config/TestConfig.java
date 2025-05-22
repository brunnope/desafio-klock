package com.klock.desafio.config;

import com.klock.desafio.entities.Cliente;
import com.klock.desafio.entities.Item;
import com.klock.desafio.entities.Pedido;
import com.klock.desafio.services.cliente.ClienteService;
import com.klock.desafio.services.item.ItemService;
import com.klock.desafio.services.pedido.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemService itemService;


    @Override
    public void run(String... args) throws Exception {

        Cliente cliente = clienteService.buscarPorId(Long.valueOf(6));
        cliente.setVip(false);

        // Criação de itens
        Item item1 = new Item("Produto A", BigDecimal.valueOf(50.00), 2, 100, null);
        Item item2 = new Item("Produto B", BigDecimal.valueOf(30.00), 1, 50, null);


        // Criação de um pedido
        Pedido pedido = new Pedido(BigDecimal.valueOf(0), BigDecimal.valueOf(0), false, LocalDate.now(), cliente);
        pedido.getItens().addAll(List.of(item1, item2));
        item1.setPedido(pedido);
        item2.setPedido(pedido);

        pedidoService.salvarPedido(pedido);

        System.out.println("Pedido criado com sucesso: " + pedido);
        System.out.println("Itens do pedido:");
        pedido.getItens().forEach(item -> System.out.println("Item: " + item.getNome() + ", Quantidade: " + item.getQuantidade()));


    }
}