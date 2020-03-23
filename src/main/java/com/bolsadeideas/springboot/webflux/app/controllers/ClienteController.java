package com.bolsadeideas.springboot.webflux.app.controllers;

import com.bolsadeideas.springboot.webflux.app.models.documents.Cliente;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//controller client
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:4200"}, allowedHeaders = "*")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;


    @GetMapping()
    public Mono<ResponseEntity<Flux<Cliente>>> listarClientes() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(clienteService.findAll())
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Cliente>> verCliente(@PathVariable String id) {
        return clienteService.findById(id).map(p -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> crearCliente(@Valid @RequestBody Mono<Cliente> monoCliente) {

        Map<String, Object> respuesta = new HashMap<String, Object>();

        return monoCliente.flatMap(cliente -> {
            if (cliente.getCreateAt() == null) {
                cliente.setCreateAt(new Date());
            }

            return clienteService.save(cliente).map(p -> {
                respuesta.put("cliente", p);
                respuesta.put("mensaje", "Cliente creado con éxito!!!!!");
                respuesta.put("timestamp", new Date());
                return ResponseEntity
                        .created(URI.create("/api/cientes/".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(respuesta);
            });

        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        respuesta.put("errors", list);
                        respuesta.put("timestamp", new Date());
                        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(respuesta));
                    });

        });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Cliente>> editarCliente(@RequestBody Cliente cliente, @PathVariable String id) {
        return clienteService.findById(id).flatMap(c -> {
            c.setNombre(cliente.getNombre());
            c.setApellido(cliente.getApellido());
            c.setEmail(cliente.getEmail());
            return clienteService.save(c);
        }).map(p -> ResponseEntity.created(URI.create("/api/clientes/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminarCliente(@PathVariable String id) {
        return clienteService.findById(id).flatMap(p -> {
            return clienteService.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
