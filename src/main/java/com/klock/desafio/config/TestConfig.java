package com.klock.desafio.config;

import com.klock.desafio.services.cliente.ClienteService;
import com.klock.desafio.services.item.ItemService;
import com.klock.desafio.services.notificacao.NotificacaoService;
import com.klock.desafio.services.pedido.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Override
    public void run(String... args) throws Exception {
        /*
        Mensagem mensagem = new Mensagem("cicero.brunno@academico.ifpb.edu.br", "TESTE",
                "Testando...");

        notificacaoService.enviarEmail(mensagem);
        */

        /*
        Cliente cliente = clienteService.buscarPorId(Long.valueOf(1));
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

        */
    }
}