package com.example.b1eftb.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.b1eftb.model.OrdenDeCompra;
import com.example.b1eftb.service.OrdenDeCompraService;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenDeCompraController {

    private static final Logger logger = LogManager.getLogger(OrdenDeCompraController.class);

    private final OrdenDeCompraService ordenDeCompraService;

    public OrdenDeCompraController(OrdenDeCompraService ordenDeCompraService) {
        this.ordenDeCompraService = ordenDeCompraService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<OrdenDeCompra>>> getAllOrdenes() {
        List<EntityModel<OrdenDeCompra>> ordenes = ordenDeCompraService.obtenerTodasLasOrdenes().stream()
            .map(orden -> EntityModel.of(orden,
                linkTo(methodOn(OrdenDeCompraController.class).getOrdenById(orden.getId())).withSelfRel(),
                linkTo(methodOn(OrdenDeCompraController.class).getAllOrdenes()).withRel("ordenes")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(ordenes,
                linkTo(methodOn(OrdenDeCompraController.class).getAllOrdenes()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrdenDeCompra>> getOrdenById(@PathVariable Long id) {
        Optional<OrdenDeCompra> orden = ordenDeCompraService.obtenerOrdenPorId(id);
        EntityModel<OrdenDeCompra> entityModel = EntityModel.of(orden.get(),
            linkTo(methodOn(OrdenDeCompraController.class).getOrdenById(id)).withSelfRel(),
            linkTo(methodOn(OrdenDeCompraController.class).getAllOrdenes()).withRel("ordenes"));

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<OrdenDeCompra>> createOrden(@RequestBody OrdenDeCompra orden) {
        OrdenDeCompra savedOrden = ordenDeCompraService.crearOrden(orden);
        EntityModel<OrdenDeCompra> entityModel = EntityModel.of(savedOrden,
            linkTo(methodOn(OrdenDeCompraController.class).getOrdenById(savedOrden.getId())).withSelfRel(),
            linkTo(methodOn(OrdenDeCompraController.class).getAllOrdenes()).withRel("ordenes"));

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<OrdenDeCompra>> updateOrden(@PathVariable Long id, @RequestBody OrdenDeCompra orden) {
        OrdenDeCompra updatedOrden = ordenDeCompraService.actualizarOrden(orden);
        EntityModel<OrdenDeCompra> entityModel = EntityModel.of(updatedOrden,
            linkTo(methodOn(OrdenDeCompraController.class).getOrdenById(id)).withSelfRel(),
            linkTo(methodOn(OrdenDeCompraController.class).getAllOrdenes()).withRel("ordenes"));

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrden(@PathVariable Long id) {
        ordenDeCompraService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{id}")
    public String obtenerEstadoDeOrdenPorId(@PathVariable Long id) {
        Optional<OrdenDeCompra> orden = ordenDeCompraService.obtenerOrdenPorId(id);
        return orden.get().getEstado().toString();
    }
}