package com.klock.desafio.config;

import com.klock.desafio.controllers.ClienteController;
import com.klock.desafio.entities.Cliente;
import com.klock.desafio.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        Cliente cliente = new Cliente("cicero2", "cicero121@gmail.com");

        //clienteRepository.save(cliente);

    }
}
