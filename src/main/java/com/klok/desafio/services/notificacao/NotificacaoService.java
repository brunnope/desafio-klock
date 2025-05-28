package com.klok.desafio.services.notificacao;

import org.apache.commons.mail.EmailException;

public interface NotificacaoService {
    void enviarEmail(Mensagem mensagem) throws EmailException;
}
