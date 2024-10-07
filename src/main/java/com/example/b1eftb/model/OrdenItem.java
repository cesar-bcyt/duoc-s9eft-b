package com.example.b1eftb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "ORDEN_ITEMS")
public class OrdenItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDEN_ITEM_ID")
    private Long id;

    @JsonBackReference(value="orden-id")
    @ManyToOne
    @JoinColumn(name = "ORDEN_ID", nullable = false)
    private OrdenDeCompra orden;

    @JsonBackReference(value="item-id")
    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Positive(message = "La cantidad debe ser positiva")
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    public OrdenItem() {}

    public OrdenItem(OrdenDeCompra orden, Item item, Integer quantity) {
        this.orden = orden;
        this.item = item;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdenDeCompra getOrden() {
        return orden;
    }

    public void setOrden(OrdenDeCompra orden) {
        this.orden = orden;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}