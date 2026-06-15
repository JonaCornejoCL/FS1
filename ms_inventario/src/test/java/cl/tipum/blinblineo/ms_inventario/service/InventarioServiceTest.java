package cl.tipum.blinblineo.ms_inventario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.repository.InventarioRepository;

public class InventarioServiceTest {

    @Mock // Simula la base de datos (Supabase) para no tocarla
    private InventarioRepository inventarioRepository;

    @InjectMocks // Inyecta el mock en el servicio real
    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReducirStock_Exitoso() {
        // 1. GIVEN (dado que el producto cuenta con 50 unidades)
        String sku = "BLIN-CH-001";
        Inventario inventarioMock = new Inventario();
        inventarioMock.setSku(sku);
        inventarioMock.setCantidad(50);

        when(inventarioRepository.findBySku(sku)).thenReturn(inventarioMock);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioMock);

        // 2. WHEN (Cuando el cliente compra 2 unidades)
        Inventario resultado = inventarioService.reducirStock(sku, 2);

        // 3. THEN (Entonces el stock debe bajar a 48)
        assertNotNull(resultado);
        assertEquals(48, resultado.getCantidad());
        verify(inventarioRepository, times(1)).save(inventarioMock);
    }

    @Test
    void testReducirStock_FallaPorFaltaDeStock() {
        // 1. GIVEN (dado que solo tenemos 10 unidades)
        String sku = "BLIN-CH-002";
        Inventario inventarioMock = new Inventario();
        inventarioMock.setSku(sku);
        inventarioMock.setCantidad(10);

        when(inventarioRepository.findBySku(sku)).thenReturn(inventarioMock);

        // 2. WHEN & THEN (Cuando intentamos comprar 50, Entonces lanza excepción)
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            inventarioService.reducirStock(sku, 50);
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(inventarioRepository, never()).save(any()); // Verifica que no se guarda nada
    }
}