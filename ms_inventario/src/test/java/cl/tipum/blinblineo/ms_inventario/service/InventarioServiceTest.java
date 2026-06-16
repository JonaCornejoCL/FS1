package cl.tipum.blinblineo.ms_inventario.service;

import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void debeListarTodosLosInventarios() {
        Inventario i1 = new Inventario(); i1.setSku("SKU1");
        Inventario i2 = new Inventario(); i2.setSku("SKU2");
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(i1, i2));

        List<Inventario> resultado = inventarioService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(inventarioRepository).findAll();
    }

    @Test
    void obtenerPorSku_debeRetornarInventarioSiExiste() {
        Inventario inventario = new Inventario();
        inventario.setSku("SKU1");
        when(inventarioRepository.findBySku("SKU1")).thenReturn(inventario);

        Inventario resultado = inventarioService.obtenerPorSku("SKU1");

        assertNotNull(resultado);
        assertEquals("SKU1", resultado.getSku());
    }

    @Test
    void obtenerPorSku_debeLanzarExcepcionSiNoExiste() {
        when(inventarioRepository.findBySku("FALSO")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> inventarioService.obtenerPorSku("FALSO"));
    }

    @Test
    void inicializarStock_debeGuardarSiNoExiste() {
        Inventario nuevo = new Inventario();
        nuevo.setSku("NUEVO-SKU");
        when(inventarioRepository.findBySku("NUEVO-SKU")).thenReturn(null);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(nuevo);

        Inventario resultado = inventarioService.inicializarStock(nuevo);

        assertNotNull(resultado);
        verify(inventarioRepository).save(nuevo);
    }

    @Test
    void inicializarStock_debeLanzarExcepcionSiYaExiste() {
        Inventario existente = new Inventario();
        existente.setSku("EXISTE");
        when(inventarioRepository.findBySku("EXISTE")).thenReturn(existente);

        assertThrows(IllegalArgumentException.class, () -> inventarioService.inicializarStock(existente));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void reducirStock_debeReducirExitosamente() {
        Inventario inicial = new Inventario();
        inicial.setSku("SKU1");
        inicial.setCantidad(10);
        
        when(inventarioRepository.findBySku("SKU1")).thenReturn(inicial);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inicial);

        Inventario resultado = inventarioService.reducirStock("SKU1", 5);

        assertEquals(5, resultado.getCantidad());
    }

    @Test
    void reducirStock_debeLanzarExcepcionPorStockInsuficiente() {
        Inventario inicial = new Inventario();
        inicial.setSku("SKU1");
        inicial.setCantidad(2);
        when(inventarioRepository.findBySku("SKU1")).thenReturn(inicial);

        assertThrows(IllegalStateException.class, () -> inventarioService.reducirStock("SKU1", 5));
    }
}