package com.klok.desafio.services.notificacao;

import jakarta.annotation.PostConstruct;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoServiceImpl extends SimpleEmail implements NotificacaoService {

    @Value("${email.usuario}")
    private String usuario;

    @Value("${email.senha}")
    private String senha;

    @PostConstruct
    public void init() {
        setHostName("smtp.gmail.com");
        setSmtpPort(587);
        setAuthenticator(new DefaultAuthenticator(usuario, senha));
        setStartTLSEnabled(true);
        try {
            setFrom(usuario);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    public void enviarEmail(Mensagem mensagem) throws EmailException {
        setSubject(mensagem.getTitulo());
        setMsg(mensagem.getConteudo());
        addTo(mensagem.getDestinatario());
        send();
    }
}
