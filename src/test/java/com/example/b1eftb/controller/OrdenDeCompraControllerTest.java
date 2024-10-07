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

import com.example.b1eftb.model.OrdenDeCompra;
import com.example.b1eftb.service.OrdenDeCompraService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrdenDeCompraController.class)
class OrdenDeCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenDeCompraService ordenDeCompraService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrdenDeCompra orden;

    @BeforeEach
    void setUp() {
        orden = new OrdenDeCompra();
        orden.setId(1L);
        orden.setEstado(OrdenDeCompra.EstadoOrden.Creada);
    }

    @Test
    void testGetAllOrdenes() throws Exception {
        when(ordenDeCompraService.obtenerTodasLasOrdenes()).thenReturn(Arrays.asList(orden));

        mockMvc.perform(get("/api/ordenes"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$._embedded.ordenDeCompraList[0].id").value(1))
               .andExpect(jsonPath("$._embedded.ordenDeCompraList[0].estado").value("Creada"))
               .andExpect(jsonPath("$._embedded.ordenDeCompraList[0]._links.self.href").value("http://localhost/api/ordenes/1"))
               .andExpect(jsonPath("$._embedded.ordenDeCompraList[0]._links.ordenes.href").value("http://localhost/api/ordenes"))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/ordenes"));
    }

    @Test
    void testGetOrdenById() throws Exception {
        when(ordenDeCompraService.obtenerOrdenPorId(1L)).thenReturn(Optional.of(orden));

        mockMvc.perform(get("/api/ordenes/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.estado").value("Creada"))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/ordenes/1"))
               .andExpect(jsonPath("$._links.ordenes.href").value("http://localhost/api/ordenes"));
    }

    @Test
    void testCreateOrden() throws Exception {
        when(ordenDeCompraService.crearOrden(any(OrdenDeCompra.class))).thenReturn(orden);

        mockMvc.perform(post("/api/ordenes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(orden)))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.estado").value("Creada"))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/ordenes/1"))
               .andExpect(jsonPath("$._links.ordenes.href").value("http://localhost/api/ordenes"));
    }

    @Test
    void testUpdateOrden() throws Exception {
        when(ordenDeCompraService.actualizarOrden(any(OrdenDeCompra.class))).thenReturn(orden);

        mockMvc.perform(put("/api/ordenes/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(orden)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaTypes.HAL_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.estado").value("Creada"))
               .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/ordenes/1"))
               .andExpect(jsonPath("$._links.ordenes.href").value("http://localhost/api/ordenes"));
    }

    @Test
    void testDeleteOrden() throws Exception {
        doNothing().when(ordenDeCompraService).eliminarOrden(1L);

        mockMvc.perform(delete("/api/ordenes/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerEstadoDeOrdenPorId() throws Exception {
        when(ordenDeCompraService.obtenerOrdenPorId(1L)).thenReturn(Optional.of(orden));

        mockMvc.perform(get("/api/ordenes/estado/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Creada"));
    }
}