package cl.tipum.blinblineo.ms_ventas.service;

import cl.tipum.blinblineo.ms_ventas.client.CatalogoClient;
import cl.tipum.blinblineo.ms_ventas.client.InventarioClient;
import cl.tipum.blinblineo.ms_ventas.dto.VentaDTOMapper;
import cl.tipum.blinblineo.ms_ventas.dto.VentaRequestDTO;
import cl.tipum.blinblineo.ms_ventas.dto.VentaResponseDTO;
import cl.tipum.blinblineo.ms_ventas.exceptions.RecursoNoEncontradoException;
import cl.tipum.blinblineo.ms_ventas.exceptions.RecursoYaExisteException;
import cl.tipum.blinblineo.ms_ventas.model.Venta;
import cl.tipum.blinblineo.ms_ventas.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock private VentaRepository ventaRepository;
    @Mock private VentaDTOMapper ventaDTOMapper;
    @Mock private CatalogoClient catalogoClient;
    @Mock private InventarioClient inventarioClient;

    @InjectMocks private VentaService ventaService;

    @Test
    void obtenerTodasLasVentas_debeRetornarLista() {
        when(ventaRepository.findAll()).thenReturn(List.of(new Venta()));
        when(ventaDTOMapper.toDTO(any())).thenReturn(new VentaResponseDTO());
        
        assertFalse(ventaService.obtenerTodasLasVentas().isEmpty());
    }

    @Test
    void obtenerTodasLasVentas_debeLanzarExcepcion() {
        when(ventaRepository.findAll()).thenReturn(List.of());
        
        assertThrows(RecursoNoEncontradoException.class, () -> ventaService.obtenerTodasLasVentas());
    }

    @Test
    void procesarVenta_debeGuardarVenta() {
        // Simulamos el DTO para evitar crear los detalles anidados a mano
        VentaRequestDTO request = mock(VentaRequestDTO.class);
        when(request.getFolioBoleta()).thenReturn("FOLIO1");
        when(request.getDetalles()).thenReturn(List.of()); 
        
        when(ventaRepository.existsByFolioBoleta("FOLIO1")).thenReturn(false);
        when(ventaRepository.save(any(Venta.class))).thenReturn(new Venta());
        when(ventaDTOMapper.toDTO(any())).thenReturn(new VentaResponseDTO());

        assertNotNull(ventaService.procesarVenta(request));
    }

    @Test
    void procesarVenta_debeLanzarExcepcionSiExiste() {
        VentaRequestDTO request = mock(VentaRequestDTO.class);
        when(request.getFolioBoleta()).thenReturn("FOLIO1");
        when(ventaRepository.existsByFolioBoleta("FOLIO1")).thenReturn(true);

        assertThrows(RecursoYaExisteException.class, () -> ventaService.procesarVenta(request));
    }

    @Test
    void obtenerVentaPorFolio_debeRetornarVenta() {
        when(ventaRepository.findByFolioBoleta("FOLIO1")).thenReturn(new Venta());
        when(ventaDTOMapper.toDTO(any())).thenReturn(new VentaResponseDTO());
        
        assertNotNull(ventaService.obtenerVentaPorFolio("FOLIO1"));
    }

    @Test
    void obtenerVentaPorFolio_debeLanzarExcepcion() {
        when(ventaRepository.findByFolioBoleta("FOLIO1")).thenReturn(null);
        
        assertThrows(RecursoNoEncontradoException.class, () -> ventaService.obtenerVentaPorFolio("FOLIO1"));
    }

    @Test
    void actualizarVenta_debeActualizar() {
        VentaRequestDTO request = mock(VentaRequestDTO.class);
        when(request.getFolioBoleta()).thenReturn("FOLIO1");
        when(request.getDetalles()).thenReturn(List.of());

        when(ventaRepository.findByFolioBoleta("FOLIO1")).thenReturn(new Venta());
        when(ventaRepository.save(any())).thenReturn(new Venta());
        when(ventaDTOMapper.toDTO(any())).thenReturn(new VentaResponseDTO());

        assertNotNull(ventaService.actualizarVenta(request));
    }

    @Test
    void actualizarEstadoVenta_debeActualizar() {
        when(ventaRepository.findByFolioBoleta("FOLIO1")).thenReturn(new Venta());
        when(ventaRepository.save(any())).thenReturn(new Venta());
        when(ventaDTOMapper.toDTO(any())).thenReturn(new VentaResponseDTO());

        assertNotNull(ventaService.actualizarEstadoVenta("FOLIO1", "NUEVO"));
    }

    @Test
    void eliminarVenta_debeEliminar() {
        when(ventaRepository.existsByFolioBoleta("FOLIO1")).thenReturn(true);
        
        assertTrue(ventaService.eliminarVenta("FOLIO1"));
        verify(ventaRepository).deleteByFolioBoleta("FOLIO1");
    }
}