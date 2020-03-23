package com.bolsadeideas.springboot.webflux.app.models.dao;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.bolsadeideas.springboot.webflux.app.models.documents.Cliente;

public interface ClienteDao extends ReactiveMongoRepository<Cliente, String> {

}
