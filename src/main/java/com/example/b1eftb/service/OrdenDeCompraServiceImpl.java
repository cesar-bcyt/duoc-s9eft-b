package com.example.b1eftb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.b1eftb.model.Item;
import com.example.b1eftb.model.OrdenDeCompra;
import com.example.b1eftb.model.OrdenItem;
import com.example.b1eftb.repository.ItemRepository;
import com.example.b1eftb.repository.OrdenDeCompraRepository;
import com.example.b1eftb.repository.OrdenItemRepository;

@Service
public class OrdenDeCompraServiceImpl implements OrdenDeCompraService {

    @Autowired
    private final OrdenDeCompraRepository ordenDeCompraRepository;
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final OrdenItemRepository ordenItemRepository;

    public OrdenDeCompraServiceImpl(OrdenDeCompraRepository ordenDeCompraRepository, ItemRepository itemRepository, OrdenItemRepository ordenItemRepository) {
        this.ordenDeCompraRepository = ordenDeCompraRepository;
        this.itemRepository = itemRepository;
        this.ordenItemRepository = ordenItemRepository;
    }

    @Override
    public List<OrdenDeCompra> obtenerTodasLasOrdenes() {
        return ordenDeCompraRepository.findAll();
    }

    @Override
    public Optional<OrdenDeCompra> obtenerOrdenPorId(Long id) {
        return ordenDeCompraRepository.findById(id);
    }

    @Override
    public OrdenDeCompra crearOrden(OrdenDeCompra orden) {
        OrdenDeCompra order = new OrdenDeCompra();

        List<OrdenItem> ordenItems = orden.getOrdenItems();
        for (OrdenItem ordenItem : ordenItems) {
            Item item = itemRepository.findById(ordenItem.getItem().getId())
                .orElseThrow(() -> new RuntimeException("Item no encontrado: " + ordenItem.getItem().getId()));
            OrdenItem newOrdenItem = new OrdenItem();
            newOrdenItem.setItem(item);
            newOrdenItem.setQuantity(ordenItem.getQuantity());
            order.addOrdenItem(newOrdenItem);
        }
        order.setEstado(orden.getEstado());

        return ordenDeCompraRepository.save(order);
    }

    @Override
    public OrdenDeCompra actualizarOrden(OrdenDeCompra orden) {
        Optional<OrdenDeCompra> ordenExistente = obtenerOrdenPorId(orden.getId());

        // Check if the state change is legal
        if (!isEstadoChangeLegal(ordenExistente.get().getEstado(), orden.getEstado())) {
            throw new IllegalStateException("Cambio de estado no permitido: de " + ordenExistente.get().getEstado() + " a " + orden.getEstado());
        }

        // Update the state
        ordenExistente.get().setEstado(orden.getEstado());

        // Update orden items
        ordenExistente.get().getOrdenItems().clear();
        for (OrdenItem ordenItem : orden.getOrdenItems()) {
            Item item = itemRepository.findById(ordenItem.getItem().getId())
                .orElseThrow(() -> new RuntimeException("Item no encontrado: " + ordenItem.getItem().getId()));
            OrdenItem newOrdenItem = new OrdenItem();
            newOrdenItem.setItem(item);
            newOrdenItem.setQuantity(ordenItem.getQuantity());
            ordenExistente.get().addOrdenItem(newOrdenItem);
        }

        return ordenDeCompraRepository.save(ordenExistente.get());
    }

    @Override
    public void eliminarOrden(Long id) {
        Optional<OrdenDeCompra> orden = obtenerOrdenPorId(id);
        ordenDeCompraRepository.delete(orden.get());
    }

    private boolean isEstadoChangeLegal(OrdenDeCompra.EstadoOrden estadoActual, OrdenDeCompra.EstadoOrden nuevoEstado) {
        if (estadoActual == nuevoEstado) return true;
        return switch (estadoActual) {
            case Creada -> nuevoEstado == OrdenDeCompra.EstadoOrden.Pagada || nuevoEstado == OrdenDeCompra.EstadoOrden.Anulada;
            case Pagada -> nuevoEstado == OrdenDeCompra.EstadoOrden.Despachada || nuevoEstado == OrdenDeCompra.EstadoOrden.Anulada;
            case Despachada -> nuevoEstado == OrdenDeCompra.EstadoOrden.Completada;
            case Anulada, Completada -> false;
            default -> false;
        }; // No se permiten cambios desde estos estados
    }
}