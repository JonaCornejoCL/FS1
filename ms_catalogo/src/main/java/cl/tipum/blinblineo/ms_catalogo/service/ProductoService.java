package cl.tipum.blinblineo.ms_catalogo.service;
 
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 
import cl.tipum.blinblineo.ms_catalogo.model.Producto;
import cl.tipum.blinblineo.ms_catalogo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
 
/**
 * CORRECCIONES APLICADAS:
 * - Logs estructurados con Logger en puntos clave del flujo
 * - Trazabilidad de operaciones
 */
@Service
@RequiredArgsConstructor
public class ProductoService {
 
    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);
    private final ProductoRepository productoRepository;
 
    // 1. Obtiene la vitrina
    public List<Producto> obtenerTodos() {
        log.info("Consultando todos los productos del catálogo");
        List<Producto> productos = productoRepository.findAll();
        log.info("Se encontraron {} productos en el catálogo", productos.size());
        return productos;
    }
 
    // 2. Busca un producto específico por su SKU
    public Producto obtenerPorSku(String sku) {
        log.info("Buscando producto con SKU: {}", sku);
        Producto producto = productoRepository.findBySku(sku);
        
        if (producto == null) {
            log.warn("Producto no encontrado para SKU: {}", sku);
            throw new IllegalArgumentException("No se encontró ningún producto con el SKU: " + sku);
        }
        
        log.info("Producto encontrado: {} - {}", sku, producto.getNombre());
        return producto;
    }
 
    // 3. Guarda un nuevo producto en la base de datos
    public Producto guardar(Producto producto) {
        log.info("Intentando crear nuevo producto con SKU: {}", producto.getSku());
        
        // Validación extra: Evita duplicar un SKU si ya existe en la vitrina
        Producto existente = productoRepository.findBySku(producto.getSku());
        if (existente != null) {
            log.warn("Intento de duplicar SKU: {}. Ya existe en el catálogo", producto.getSku());
            throw new IllegalArgumentException("Ya existe un producto registrado con el SKU: " + producto.getSku());
        }
        
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado exitosamente - SKU: {}, Nombre: {}, Precio: ${}", 
                 guardado.getSku(), guardado.getNombre(), guardado.getPrecio());
        return guardado;
    }
 
    // 4. Actualiza los datos de un producto existente por su SKU
    public Producto actualizar(String sku, Producto productoActualizado) {
        log.info("Actualizando producto con SKU: {}", sku);
        
        // Reutiliza tu validación: si no existe, lanza la excepción de inmediato
        Producto productoExistente = obtenerPorSku(sku);
        
        // Actualiza los datos con lo que viene del JSON
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setEstado(productoActualizado.getEstado());
        
        Producto actualizado = productoRepository.save(productoExistente);
        log.info("Producto actualizado exitosamente - SKU: {}", sku);
        return actualizado;
    }
 
    // 5. Elimina un producto físico de la base de datos por su SKU
    public void eliminar(String sku) {
        log.info("Intentando eliminar producto con SKU: {}", sku);
        
        // Si no existe, pincha antes de intentar borrar
        Producto producto = obtenerPorSku(sku);
        productoRepository.delete(producto);
        
        log.info("Producto eliminado exitosamente - SKU: {}", sku);
    }
}