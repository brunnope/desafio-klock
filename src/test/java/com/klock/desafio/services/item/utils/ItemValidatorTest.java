package com.klock.desafio.services.item.utils;

import com.klock.desafio.entities.Item;
import com.klock.desafio.exceptions.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemValidatorTest {

    @InjectMocks
    private ItemValidator itemValidator;

    @Test
    @DisplayName( "Deve validar item com sucesso")
    void validarItem_comSucesso() {

        Item item = new Item();
        item.setPreco(new BigDecimal(100));
        item.setQuantidade(1);

        itemValidator.validarItem(item);

        assertEquals(1, item.getQuantidade());
        assertEquals(new BigDecimal(100), item.getPreco());

    }

    @Test
    @DisplayName( "Deve lançar BusinessRuleException para item com quantidade inválida")
    void validarItemFalha_quandoQuantidadeInvalida() {

        Item item = new Item();
        item.setPreco(new BigDecimal(100));
        item.setQuantidade(-1);

        assertThrows(BusinessRuleException.class, () -> itemValidator.validarItem(item));

    }

    @Test
    @DisplayName( "Deve lançar BusinessRuleException para item com preço inválido")
    void validarItemFalha_quandoPrecoInvalido() {

        Item item = new Item();
        item.setPreco(new BigDecimal(-1));
        item.setQuantidade(1);

        assertThrows(BusinessRuleException.class, () -> itemValidator.validarItem(item));

    }
}