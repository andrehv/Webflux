package com.bolsadeideas.springboot.webflux.app.models.services;

import com.bolsadeideas.springboot.webflux.app.models.dao.ClienteDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Cliente;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private ClienteDao clienteDao;

    @Override
    public Flux<Cliente> findAll() {
        return clienteDao.findAll();
    }

    @Override
    public Mono<Cliente> findById(String id) {
        return clienteDao.findById(id);
    }

    @Override
    public Mono<Cliente> save(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    public Mono<Void> delete(Cliente cliente) {
        return clienteDao.delete(cliente);
    }
}
