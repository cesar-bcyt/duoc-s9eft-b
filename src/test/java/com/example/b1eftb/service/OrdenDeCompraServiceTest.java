package com.example.b1eftb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.b1eftb.model.Item;
import com.example.b1eftb.model.OrdenDeCompra;
import com.example.b1eftb.model.OrdenItem;
import com.example.b1eftb.repository.ItemRepository;
import com.example.b1eftb.repository.OrdenDeCompraRepository;
import com.example.b1eftb.repository.OrdenItemRepository;

class OrdenDeCompraServiceTest {

    @Mock
    private OrdenDeCompraRepository ordenDeCompraRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrdenItemRepository ordenItemRepository;

    @InjectMocks
    private OrdenDeCompraServiceImpl ordenDeCompraService;

    private OrdenDeCompra orden;
    private Item item;
    private OrdenItem ordenItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item("Test Item", 1000);
        item.setId(1L);
        ordenItem = new OrdenItem();
        ordenItem.setItem(item);
        ordenItem.setQuantity(2);
        orden = new OrdenDeCompra();
        orden.setId(1L);
        orden.setEstado(OrdenDeCompra.EstadoOrden.Creada);
        orden.addOrdenItem(ordenItem);
    }

    @Test
    void testCrearOrden() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(ordenDeCompraRepository.save(any(OrdenDeCompra.class))).thenReturn(orden);

        OrdenDeCompra savedOrden = ordenDeCompraService.crearOrden(orden);

        assertNotNull(savedOrden);
        assertEquals(orden.getId(), savedOrden.getId());
        assertEquals(orden.getEstado(), savedOrden.getEstado());
        assertEquals(1, savedOrden.getOrdenItems().size());

        verify(itemRepository).findById(1L);
        verify(ordenDeCompraRepository).save(any(OrdenDeCompra.class));
    }

    @Test
    void testObtenerOrdenPorId() {
        when(ordenDeCompraRepository.findById(1L)).thenReturn(Optional.of(orden));

        Optional<OrdenDeCompra> foundOrden = ordenDeCompraService.obtenerOrdenPorId(1L);

        assertTrue(foundOrden.isPresent());
        assertEquals(orden.getId(), foundOrden.get().getId());

        verify(ordenDeCompraRepository).findById(1L);
    }

    @Test
    void testObtenerTodasLasOrdenes() {
        List<OrdenDeCompra> ordenes = Arrays.asList(orden, new OrdenDeCompra());
        when(ordenDeCompraRepository.findAll()).thenReturn(ordenes);

        List<OrdenDeCompra> foundOrdenes = ordenDeCompraService.obtenerTodasLasOrdenes();

        assertNotNull(foundOrdenes);
        assertEquals(2, foundOrdenes.size());

        verify(ordenDeCompraRepository).findAll();
    }

    @Test
    void testActualizarOrden() {
        OrdenDeCompra updatedOrden = new OrdenDeCompra();
        updatedOrden.setId(1L);
        updatedOrden.setEstado(OrdenDeCompra.EstadoOrden.Pagada);

        when(ordenDeCompraRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(ordenDeCompraRepository.save(any(OrdenDeCompra.class))).thenReturn(updatedOrden);

        OrdenDeCompra result = ordenDeCompraService.actualizarOrden(updatedOrden);

        assertNotNull(result);
        assertEquals(updatedOrden.getEstado(), result.getEstado());

        verify(ordenDeCompraRepository).findById(1L);
        verify(ordenDeCompraRepository).save(any(OrdenDeCompra.class));
    }

    @Test
    void testEliminarOrden() {
        when(ordenDeCompraRepository.findById(1L)).thenReturn(Optional.of(orden));
        doNothing().when(ordenDeCompraRepository).delete(orden);

        ordenDeCompraService.eliminarOrden(1L);

        verify(ordenDeCompraRepository).findById(1L);
        verify(ordenDeCompraRepository).delete(orden);
    }

    @Test
    void testActualizarOrdenEstadoIlegal() {
        OrdenDeCompra updatedOrden = new OrdenDeCompra();
        updatedOrden.setId(1L);
        updatedOrden.setEstado(OrdenDeCompra.EstadoOrden.Completada);

        when(ordenDeCompraRepository.findById(1L)).thenReturn(Optional.of(orden));

        assertThrows(IllegalStateException.class, () -> ordenDeCompraService.actualizarOrden(updatedOrden));

        verify(ordenDeCompraRepository).findById(1L);
    }
}