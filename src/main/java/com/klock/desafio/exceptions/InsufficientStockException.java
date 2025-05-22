package com.klock.desafio.exceptions;

public class InsufficientStockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientStockException() {
        super("Estoque insuficinete para os itens do pedido!");
    }
}