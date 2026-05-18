package cl.tipum.blinblineo.ms_inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // <-- Importación para los logs

@Service
@RequiredArgsConstructor
@Slf4j // <-- Anotación de Lombok para activar la trazabilidad
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    // 1. Ver todo el stock
    public List<Inventario> obtenerTodos() {
        log.info("Consultando todo el stock disponible en bodega");
        return inventarioRepository.findAll();
    }

    // 2. Buscar stock de un SKU específico
    public Inventario obtenerPorSku(String sku) {
        log.info("Buscando stock en bodega para el SKU: {}", sku);
        Inventario inventario = inventarioRepository.findBySku(sku);
        if (inventario == null) {
            log.error("Error: No se encontró el SKU {} en los registros de inventario", sku);
            throw new IllegalArgumentException("No hay registro de inventario para el SKU: " + sku);
        }
        return inventario;
    }

    // 3. Inicializar stock de una prenda nueva
    public Inventario inicializarStock(Inventario inventario) {
        log.info("Iniciando registro de stock para nuevo SKU: {}", inventario.getSku());
        // Validación de seguridad para no duplicar filas del mismo producto
        Inventario existente = inventarioRepository.findBySku(inventario.getSku());
        if (existente != null) {
            log.warn("Intento de duplicación detectado para SKU: {}", inventario.getSku());
            throw new IllegalArgumentException("El SKU " + inventario.getSku() + " ya existe en bodega.");
        }
        log.info("Stock inicializado exitosamente para SKU: {}", inventario.getSku());
        return inventarioRepository.save(inventario);
    }

    // 4. Restar stock cuando hay una venta
    public Inventario reducirStock(String sku, Integer cantidadComprada) {
        log.info("Iniciando solicitud de rebaja de stock para SKU: {} - Cantidad solicitada: {}", sku, cantidadComprada);
        // Reutiliza tu propia validación
        Inventario inventario = obtenerPorSku(sku);
        
        // Verifica que haya suficiente ropa en bodega
        if (inventario.getCantidad() < cantidadComprada) {
            log.warn("Quiebre de stock detectado para SKU: {}. Disponible: {}, Solicitado: {}", 
                     sku, inventario.getCantidad(), cantidadComprada);
            throw new IllegalStateException("Stock insuficiente. Solo quedan " + inventario.getCantidad() + " unidades del SKU: " + sku);
        }
        
        inventario.setCantidad(inventario.getCantidad() - cantidadComprada);
        log.info("Stock actualizado exitosamente para SKU: {}. Nuevo stock: {}", sku, inventario.getCantidad());
        return inventarioRepository.save(inventario);
    }
}