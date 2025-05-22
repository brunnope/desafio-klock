package com.klock.desafio.exceptions;

public class NotificacaoException extends RuntimeException {
    public NotificacaoException() {
        super("Erro no envio de email.");
    }
}
