package com.example.b1eftb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "ITEMS")
public class Item extends RepresentationModel<Item> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    @NotBlank(message = "El nombre del item no puede estar en blanco")
    @Column(name = "ITEM_NAME", nullable = false)
    private String name;

    @Positive(message = "El precio del ítem debe ser positivo")
    @Column(name = "ITEM_PRICE", nullable = false)
    private Integer price;

    @JsonIgnore
    @JsonManagedReference(value="item-id")
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<OrdenItem> ordenItems = new ArrayList<>();

    public Item() {}

    public Item(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}