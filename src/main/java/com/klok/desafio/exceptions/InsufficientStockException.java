package com.klok.desafio.exceptions;

public class InsufficientStockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientStockException() {
        super("Estoque insuficiente para os itens do pedido!");
    }
}