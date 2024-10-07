package com.example.b1eftb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ORDENES_DE_COMPRA")
public class OrdenDeCompra extends RepresentationModel<OrdenDeCompra> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDEN_ID")
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonManagedReference(value="orden-id")
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    private List<OrdenItem> ordenItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "ESTADO", nullable = false)
    private EstadoOrden estado;

    public static class ItemDTO {
        private Long id;
        private String name;
        private Integer price;
        private Integer quantity;

        public ItemDTO(Long id, String name, Integer price, Integer quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public Integer getPrice() { return price; }
        public Integer getQuantity() { return quantity; }

        // Setters
        public void setId(Long id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setPrice(Integer price) { this.price = price; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public List<ItemDTO> getItems() {
        return ordenItems.stream()
            .map(item -> new ItemDTO(item.getItem().getId(), item.getItem().getName(), item.getItem().getPrice(), item.getQuantity()))
            .collect(Collectors.toList());
    }

    public enum EstadoOrden {
        Creada, Anulada, Pagada, Despachada, Completada
    }

    public OrdenDeCompra() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrdenItem> getOrdenItems() {
        return ordenItems;
    }

    public void setOrdenItems(List<OrdenItem> ordenItems) {
        this.ordenItems = ordenItems;
    }

    public void addOrdenItem(OrdenItem ordenItem) {
        ordenItems.add(ordenItem);
        ordenItem.setOrden(this);
    }

    public void removeOrdenItem(OrdenItem ordenItem) {
        ordenItems.remove(ordenItem);
        ordenItem.setOrden(null);
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden nuevoEstado) {
        this.estado = nuevoEstado;
    }
}