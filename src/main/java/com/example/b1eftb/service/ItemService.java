package com.example.b1eftb.service;

import java.util.List;
import java.util.Optional;

import com.example.b1eftb.model.Item;

public interface ItemService {
    Item crearItem(Item item);
    Optional<Item> obtenerItemPorId(Long id);
    List<Item> obtenerTodosLosItems();
    Item actualizarItem(Item item);
    void eliminarItem(Long id);
}