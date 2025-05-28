package com.klok.desafio.services.notificacao;

public class Mensagem {

    private String destinatario;
    private String titulo;
    private String conteudo;

    public Mensagem(String destinatario, String titulo, String conteudo) {
        this.destinatario = destinatario;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
