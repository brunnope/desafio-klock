package com.klock.desafio.services.pedido.utils;

import com.klock.desafio.entities.Item;
import com.klock.desafio.entities.Pedido;
import com.klock.desafio.services.item.utils.ItemValidator;
import com.klock.desafio.exceptions.BusinessRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PedidoValidator {

    @Autowired
    private ItemValidator itemValidator;

    public void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new BusinessRuleException("Pedido não pode ser nulo.");
        }

        validarCliente(pedido);
        validarItens(pedido);
        validarTotais(pedido);
        validarDataEntrega(pedido);
    }

    private void validarCliente(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new BusinessRuleException("Pedido deve estar associado a um cliente.");
        }

        if (pedido.getCliente().getId() == null) {
            throw new BusinessRuleException("Cliente associado ao pedido não possui um ID válido.");
        }
    }

    private void validarItens(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new BusinessRuleException("Pedido deve conter pelo menos um item.");
        }

        for (Item item : pedido.getItens()) {
            itemValidator.validarItem(item);
        }
    }

    private void validarTotais(Pedido pedido) {
        if (pedido.getTotal() == null || pedido.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("O total do pedido deve ser maior que zero.");
        }

        if (pedido.getTotalComDesconto() == null || pedido.getTotalComDesconto().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("O total com desconto não pode ser negativo.");
        }

        if (pedido.getTotalComDesconto().compareTo(pedido.getTotal()) > 0) {
            throw new BusinessRuleException("O total com desconto não pode ser maior que o total original.");
        }
    }

    private void validarDataEntrega(Pedido pedido) {
        if (pedido.getDataEntrega() != null && pedido.getDataEntrega().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("A data de entrega não pode ser uma data no passado.");
        }
    }
}