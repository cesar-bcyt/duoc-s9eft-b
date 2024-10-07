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

import com.example.b1eftb.model.Item;
import com.example.b1eftb.service.ItemService;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private static final Logger logger = LogManager.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Item>> createItem(@RequestBody Item item) {
        Item savedItem = itemService.crearItem(item);
        EntityModel<Item> entityModel = EntityModel.of(savedItem,
            linkTo(methodOn(ItemController.class).getItemById(savedItem.getId())).withSelfRel(),
            linkTo(methodOn(ItemController.class).getAllItems()).withRel("items"));

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.obtenerItemPorId(id);
        EntityModel<Item> entityModel = EntityModel.of(item.orElse(null),
            linkTo(methodOn(ItemController.class).getItemById(id)).withSelfRel(),
            linkTo(methodOn(ItemController.class).getAllItems()).withRel("items"));

        return ResponseEntity.ok(entityModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Item>>> getAllItems() {
        List<EntityModel<Item>> items = itemService.obtenerTodosLosItems().stream()
            .map(item -> EntityModel.of(item,
                linkTo(methodOn(ItemController.class).getItemById(item.getId())).withSelfRel(),
                linkTo(methodOn(ItemController.class).getAllItems()).withRel("items")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(items,
                linkTo(methodOn(ItemController.class).getAllItems()).withSelfRel()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> updateItem(@PathVariable Long id, @RequestBody Item item) {
        Item updatedItem = itemService.actualizarItem(item);
        EntityModel<Item> entityModel = EntityModel.of(updatedItem,
            linkTo(methodOn(ItemController.class).getItemById(id)).withSelfRel(),
            linkTo(methodOn(ItemController.class).getAllItems()).withRel("items"));

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }
}