package com.klok.desafio.services.pedido.utils;

import com.klok.desafio.entities.Item;
import com.klok.desafio.entities.Pedido;
import com.klok.desafio.exceptions.InsufficientStockException;
import com.klok.desafio.exceptions.NotificacaoException;
import com.klok.desafio.services.notificacao.Mensagem;
import com.klok.desafio.services.notificacao.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProcessarPedidoService {

    private final int DIAS_ENTREGA = 3;

    @Autowired
    private CalculadoraPedidoService calculadoraPedidoService;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private PedidoValidator pedidoValidator;

    @Autowired
    private NotificacaoService notificacaoService;

    public void processarPedido(Pedido pedido) {
        calculadoraPedidoService.calcularValorTotalPedido(pedido);
        calculadoraPedidoService.calcularValorTotalComDesconto(pedido);
        verificarEstoque(pedido);
        definirDataEntrega(pedido);
        pedidoValidator.validarPedido(pedido);
        atualizarEstoque(pedido);
        enviarEmail(pedido);
    }

    private void verificarEstoque(Pedido pedido){
        if (!estoqueService.verificarEstoque(pedido)){
            throw new InsufficientStockException();
        }
        pedido.setEmEstoque(true);
    }

    private void definirDataEntrega(Pedido pedido) {
        if (pedido.getEmEstoque()){
            pedido.setDataEntrega(LocalDate.now().plusDays(DIAS_ENTREGA));
        } else {
            pedido.setDataEntrega(null);
        }
    }

    private void atualizarEstoque(Pedido pedido) {
        for (Item item : pedido.getItens()) {
            item.setEstoque(item.getEstoque() - item.getQuantidade());
        }
    }

    private void enviarEmail(Pedido pedido) {
        String conteudo = "Pedido enviado! Seu pedido será entregue em breve.";

        Mensagem mensagem = new Mensagem(pedido.getCliente().getEmail(), "Pedido enviado",
                conteudo);

        try {
            notificacaoService.enviarEmail(mensagem);
        }catch (Exception e){
            throw new NotificacaoException();
        }
    }

}
