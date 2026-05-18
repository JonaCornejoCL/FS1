package cl.tipum.blinblineo.ms_inventario.service;
 
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
 
/**
 * CORRECCIONES APLICADAS:
 * - Logs estructurados con Logger en puntos clave del flujo (IE 2.3.2)
 * - Trazabilidad de operaciones: info para operaciones exitosas, warn/error para problemas
 */
@Service
@RequiredArgsConstructor
public class InventarioService {
 
    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);
    private final InventarioRepository inventarioRepository;
 
    // 1. Ver todo el stock
    public List<Inventario> obtenerTodos() {
        log.info("Consultando todo el inventario");
        List<Inventario> inventarios = inventarioRepository.findAll();
        log.info("Se encontraron {} registros de inventario", inventarios.size());
        return inventarios;
    }
 
    // 2. Buscar stock de un SKU específico
    public Inventario obtenerPorSku(String sku) {
        log.info("Buscando inventario para SKU: {}", sku);
        Inventario inventario = inventarioRepository.findBySku(sku);
        
        if (inventario == null) {
            log.warn("No se encontró inventario para el SKU: {}", sku);
            throw new IllegalArgumentException("No hay registro de inventario para el SKU: " + sku);
        }
        
        log.info("Inventario encontrado para SKU {}: {} unidades", sku, inventario.getCantidad());
        return inventario;
    }
 
    // 3. Inicializar stock de una prenda nueva
    public Inventario inicializarStock(Inventario inventario) {
        log.info("Iniciando registro de stock para SKU: {}", inventario.getSku());
        
        // Validación de seguridad para no duplicar filas del mismo producto
        Inventario existente = inventarioRepository.findBySku(inventario.getSku());
        if (existente != null) {
            log.warn("Intento de duplicar SKU: {}. Ya existe en inventario", inventario.getSku());
            throw new IllegalArgumentException("El SKU " + inventario.getSku() + " ya existe en bodega.");
        }
        
        Inventario guardado = inventarioRepository.save(inventario);
        log.info("Stock inicializado exitosamente - SKU: {}, Cantidad: {}", 
                 guardado.getSku(), guardado.getCantidad());
        return guardado;
    }
 
    // 4. Restar stock cuando hay una venta
    public Inventario reducirStock(String sku, Integer cantidadComprada) {
        log.info("Solicitando reducción de stock - SKU: {}, Cantidad: {}", sku, cantidadComprada);
        
        // Reutiliza tu propia validación
        Inventario inventario = obtenerPorSku(sku);
        
        // Verifica que haya suficiente ropa en bodega
        if (inventario.getCantidad() < cantidadComprada) {
            log.error("Stock insuficiente para SKU: {}. Solicitado: {}, Disponible: {}", 
                     sku, cantidadComprada, inventario.getCantidad());
            throw new IllegalStateException(
                "Stock insuficiente. Solo quedan " + inventario.getCantidad() + 
                " unidades del SKU: " + sku
            );
        }
        
        int stockAnterior = inventario.getCantidad();
        inventario.setCantidad(inventario.getCantidad() - cantidadComprada);
        Inventario actualizado = inventarioRepository.save(inventario);
        
        log.info("Stock reducido exitosamente - SKU: {}, Stock anterior: {}, Stock actual: {}", 
                 sku, stockAnterior, actualizado.getCantidad());
        return actualizado;
    }
}
 