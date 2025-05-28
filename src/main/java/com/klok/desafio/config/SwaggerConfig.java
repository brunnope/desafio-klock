package com.klok.desafio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentação da API - Desafio Klok")
                        .version("1.0.0")
                        .description("- \n" +
                                "Esta API foi desenvolvida como solução para o desafio técnico Klok. " +
                                "Ela fornece endpoints REST para gerenciamento de informações de clientes, " +
                                "pedidos e itens. Permitindo, assim, operações como criação, busca, listagem, " +
                                "atualização e exclusão." +
                                "Desenvolvida com Spring Boot, a aplicação utiliza o PostgreSQL como banco de dados " +
                                "e é documentada com o Springdoc OpenAPI."));
    }
}