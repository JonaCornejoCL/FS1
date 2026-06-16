package cl.tipum.blinblineo.ms_ventas.controller;

import cl.tipum.blinblineo.ms_ventas.dto.VentaResponseDTO;
import cl.tipum.blinblineo.ms_ventas.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Corrección para Spring Boot 3.4: Evita los warnings de deprecación
    @MockitoBean
    private VentaService ventaService;

    @Test
    void listarVentas_debeRetornar200() throws Exception {
        when(ventaService.obtenerTodasLasVentas()).thenReturn(List.of(new VentaResponseDTO()));
        mockMvc.perform(get("/api/v1/ventas")).andExpect(status().isOk());
    }

    @Test
    void obtenerBoletaPorFolio_debeRetornar200() throws Exception {
        when(ventaService.obtenerVentaPorFolio("FOLIO1")).thenReturn(new VentaResponseDTO());
        mockMvc.perform(get("/api/v1/ventas/FOLIO1")).andExpect(status().isOk());
    }

    @Test
    void registrarVenta_debeRetornar201() throws Exception {
        when(ventaService.procesarVenta(any())).thenReturn(new VentaResponseDTO());
        mockMvc.perform(post("/api/v1/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizaVenta_debeRetornar200() throws Exception {
        when(ventaService.actualizarVenta(any())).thenReturn(new VentaResponseDTO());
        mockMvc.perform(put("/api/v1/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void actualizaEstadoVenta_debeRetornar200() throws Exception {
        when(ventaService.actualizarEstadoVenta("FOLIO1", "ESTADO")).thenReturn(new VentaResponseDTO());
        mockMvc.perform(put("/api/v1/ventas/estados")
                .param("folio", "FOLIO1")
                .param("estado", "ESTADO"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarVenta_debeRetornar204() throws Exception {
        when(ventaService.eliminarVenta("FOLIO1")).thenReturn(true);
        mockMvc.perform(delete("/api/v1/ventas/FOLIO1")).andExpect(status().isNoContent());
    }
}