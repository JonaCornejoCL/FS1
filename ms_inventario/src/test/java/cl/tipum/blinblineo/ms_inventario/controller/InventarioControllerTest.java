package cl.tipum.blinblineo.ms_inventario.controller;

import cl.tipum.blinblineo.ms_inventario.dto.InventarioDTO;
import cl.tipum.blinblineo.ms_inventario.dto.InventarioDTOMapper;
import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private InventarioDTOMapper dtoMapper;

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        Inventario inventario = new Inventario();
        when(inventarioService.obtenerTodos()).thenReturn(List.of(inventario));
        when(dtoMapper.toDTO(any())).thenReturn(InventarioDTO.builder().build());

        mockMvc.perform(get("/api/v1/inventario"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorSku_debeRetornar200() throws Exception {
        Inventario inventario = new Inventario();
        when(inventarioService.obtenerPorSku("SKU1")).thenReturn(inventario);
        when(dtoMapper.toDTO(any())).thenReturn(InventarioDTO.builder().sku("SKU1").build());

        mockMvc.perform(get("/api/v1/inventario/SKU1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU1"));
    }

    @Test
    void inicializarStock_debeRetornar201() throws Exception {
        Inventario inventario = new Inventario();
        when(dtoMapper.toEntity(any())).thenReturn(inventario);
        when(inventarioService.inicializarStock(any())).thenReturn(inventario);
        when(dtoMapper.toDTO(any())).thenReturn(InventarioDTO.builder().sku("NUEVO").build());

        // Simulamos un POST con un cuerpo JSON
        mockMvc.perform(post("/api/v1/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sku\":\"NUEVO\", \"cantidad\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("NUEVO"));
    }

    @Test
    void reducirStock_debeRetornar200() throws Exception {
        Inventario inventario = new Inventario();
        when(inventarioService.reducirStock(anyString(), any())).thenReturn(inventario);
        when(dtoMapper.toDTO(any())).thenReturn(InventarioDTO.builder().sku("SKU1").cantidad(5).build());

        // Simulamos el PUT con el RequestParam
        mockMvc.perform(put("/api/v1/inventario/SKU1/reducir")
                .param("cantidad", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(5));
    }
}