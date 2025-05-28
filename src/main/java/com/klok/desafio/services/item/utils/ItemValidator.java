package com.klok.desafio.services.item.utils;

import com.klok.desafio.entities.Item;
import com.klok.desafio.exceptions.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ItemValidator {

    public void validarItem(Item item) {
        if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
            throw new BusinessRuleException("Item do pedido possui uma quantidade inválida.");
        }

        if (item.getPreco() == null || item.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Item do pedido possui um preço inválido.");
        }
    }
}