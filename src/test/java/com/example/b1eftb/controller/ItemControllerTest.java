package com.example.b1eftb.controller;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.b1eftb.model.Item;
import com.example.b1eftb.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item("Test Item", 1000);
        item.setId(1L);
    }

    @Test
    void testGetAllItems() throws Exception {
        when(itemService.obtenerTodosLosItems()).thenReturn(Arrays.asList(item));

        mockMvc.perform(get("/api/items"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$._embedded.itemList[0].id").value(1))
               .andExpect(jsonPath("$._embedded.itemList[0].name").value("Test Item"))
               .andExpect(jsonPath("$._embedded.itemList[0].price").value(1000))
               .andExpect(jsonPath("$._embedded.itemList[0]._links.self.href").value("http://localhost/api/items/1"))
               .andExpect(jsonPath("$._embedded.itemList[0]._links.items.href").value("http://localhost/api/items"))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/items"));
    }

    @Test
    void testGetItemById() throws Exception {
        when(itemService.obtenerItemPorId(1L)).thenReturn(Optional.of(item));

        mockMvc.perform(get("/api/items/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Test Item"))
               .andExpect(jsonPath("$.price").value(1000))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/items/1"))
               .andExpect(jsonPath("$._links.items.href").value("http://localhost/api/items"));
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.crearItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/api/items")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(item)))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Test Item"))
               .andExpect(jsonPath("$.price").value(1000))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/items/1"))
               .andExpect(jsonPath("$._links.items.href").value("http://localhost/api/items"));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.actualizarItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(put("/api/items/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(item)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Test Item"))
               .andExpect(jsonPath("$.price").value(1000))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/items/1"))
               .andExpect(jsonPath("$._links.items.href").value("http://localhost/api/items"));
    }

    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(itemService).eliminarItem(1L);

        mockMvc.perform(delete("/api/items/1"))
               .andExpect(status().isNoContent());
    }
}