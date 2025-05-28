package com.klok.desafio.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Modelo de resposta para erros.")
public class StandardError {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "America/Sao_Paulo")
    private Instant timestamp;

    @Schema(description = "Código do status HTTP", example = "400")
    private Integer status;

    @Schema(description = "Resumo do erro", example = "Recurso não encontrado")
    private String error;

    @Schema(description = "Mensagem detalhada do erro", example = "ID do cliente não encontrado")
    private String message;

    @Schema(description = "Caminho da requisição que gerou o erro", example = "/clientes/5")
    private String path;

    public StandardError() {}

    public StandardError(Instant timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}