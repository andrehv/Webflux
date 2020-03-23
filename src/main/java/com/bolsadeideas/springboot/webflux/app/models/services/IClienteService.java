package com.bolsadeideas.springboot.webflux.app.models.services;


import com.bolsadeideas.springboot.webflux.app.models.documents.Cliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClienteService {

    public Flux<Cliente> findAll();

    public Mono<Cliente> findById(String id);

    public Mono<Cliente> save(Cliente cliente);

    public Mono<Void> delete(Cliente cliente);

}
