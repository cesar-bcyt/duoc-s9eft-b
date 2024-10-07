package com.example.b1eftb.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.b1eftb.model.Item;
import com.example.b1eftb.repository.ItemRepository;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item("Test Item", 1000);
        item.setId(1L);
    }

    @Test
    void testCrearItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item savedItem = itemService.crearItem(item);

        assertNotNull(savedItem);
        assertEquals(item.getId(), savedItem.getId());
        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getPrice(), savedItem.getPrice());

        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void testObtenerItemPorId() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Optional<Item> foundItem = itemService.obtenerItemPorId(1L);

        assertTrue(foundItem.isPresent());
        assertEquals(item.getId(), foundItem.get().getId());

        verify(itemRepository).findById(1L);
    }

    @Test
    void testObtenerItemPorIdNoExistente() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Item> foundItem = itemService.obtenerItemPorId(99L);

        assertFalse(foundItem.isPresent());

        verify(itemRepository).findById(99L);
    }

    @Test
    void testObtenerTodosLosItems() {
        List<Item> items = Arrays.asList(item, new Item("Another Item", 2000));
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> foundItems = itemService.obtenerTodosLosItems();

        assertNotNull(foundItems);
        assertEquals(2, foundItems.size());

        verify(itemRepository).findAll();
    }

    @Test
    void testObtenerTodosLosItemsListaVacia() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList());

        List<Item> foundItems = itemService.obtenerTodosLosItems();

        assertNotNull(foundItems);
        assertTrue(foundItems.isEmpty());

        verify(itemRepository).findAll();
    }

    @Test
    void testActualizarItem() {
        Item updatedItem = new Item("Updated Item", 1500);
        updatedItem.setId(1L);

        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        Item result = itemService.actualizarItem(updatedItem);

        assertNotNull(result);
        assertEquals(updatedItem.getName(), result.getName());
        assertEquals(updatedItem.getPrice(), result.getPrice());

        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void testEliminarItem() {
        doNothing().when(itemRepository).deleteById(1L);

        itemService.eliminarItem(1L);

        verify(itemRepository).deleteById(1L);
    }

    @Test
    void testEliminarItemNoExistente() {
        doThrow(new RuntimeException("Item not found")).when(itemRepository).deleteById(99L);

        assertThrows(RuntimeException.class, () -> itemService.eliminarItem(99L));

        verify(itemRepository).deleteById(99L);
    }
}