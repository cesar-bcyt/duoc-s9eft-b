package com.example.b1eftb.service;

import java.util.List;
import java.util.Optional;

import com.example.b1eftb.model.OrdenDeCompra;

public interface OrdenDeCompraService {
    OrdenDeCompra crearOrden(OrdenDeCompra orden);
    Optional<OrdenDeCompra> obtenerOrdenPorId(Long id);
    List<OrdenDeCompra> obtenerTodasLasOrdenes();
    OrdenDeCompra actualizarOrden(OrdenDeCompra orden);
    void eliminarOrden(Long id);
}