package cl.tipum.blinblineo.ms_ventas.service;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import cl.tipum.blinblineo.ms_ventas.client.CatalogoClient;
import cl.tipum.blinblineo.ms_ventas.client.InventarioClient;
import cl.tipum.blinblineo.ms_ventas.dto.VentaDTOMapper;
import cl.tipum.blinblineo.ms_ventas.dto.VentaRequestDTO;
import cl.tipum.blinblineo.ms_ventas.dto.VentaResponseDTO;
import cl.tipum.blinblineo.ms_ventas.exceptions.RecursoNoEncontradoException;
import cl.tipum.blinblineo.ms_ventas.exceptions.RecursoYaExisteException;
import cl.tipum.blinblineo.ms_ventas.model.DetalleVenta;
import cl.tipum.blinblineo.ms_ventas.model.Venta;
import cl.tipum.blinblineo.ms_ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
 
/**
 * CORRECCIONES APLICADAS:
 * - Logs estructurados con Logger en puntos clave del flujo (IE 2.3.2)
 * - Trazabilidad de operaciones: info para éxitos, warn/error para problemas
 * - Logs en llamadas Feign para debugging distribuido
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VentaService {
    
    private static final Logger log = LoggerFactory.getLogger(VentaService.class);
    
    private final VentaRepository ventaRepository;
    private final VentaDTOMapper ventaDTOMapper;
    private final CatalogoClient catalogoClient;
    private final InventarioClient inventarioClient;
 
    public List<VentaResponseDTO> obtenerTodasLasVentas() {
        log.info("Consultando todas las ventas registradas");
        List<Venta> ventas = ventaRepository.findAll();
        
        if (ventas.isEmpty()) {
            log.warn("No se encontraron ventas en el sistema");
            throw new RecursoNoEncontradoException("No se encontraron ventas registradas.");
        }
 
        log.info("Se encontraron {} ventas", ventas.size());
        return ventas.stream()
                .map(ventaDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
 
    public VentaResponseDTO procesarVenta(VentaRequestDTO request) {
        log.info("Procesando nueva venta - Folio: {}, Cliente: {}, Monto: ${}", 
                 request.getFolioBoleta(), request.getIdCliente(), request.getMontoTotal());
        
        // Validación de duplicados
        if (ventaRepository.existsByFolioBoleta(request.getFolioBoleta())) {
            log.warn("Intento de duplicar venta con folio: {}", request.getFolioBoleta());
            throw new RecursoYaExisteException("Ya existe una venta con el folio: " + request.getFolioBoleta());
        }
 
        // Validación con microservicios vía Feign
        log.debug("Validando {} productos con Catálogo e Inventario", request.getDetalles().size());
        request.getDetalles().forEach(detalle -> {
            log.debug("Validando SKU: {} - Cantidad: {}", detalle.getSkuProducto(), detalle.getCantidad());
            
            // 1. Valida que el SKU exista en el Catálogo
            try {
                catalogoClient.obtenerProductoPorSku(detalle.getSkuProducto());
                log.debug("Producto {} encontrado en catálogo", detalle.getSkuProducto());
            } catch (Exception e) {
                log.error("Error al validar producto {} en catálogo: {}", 
                         detalle.getSkuProducto(), e.getMessage());
                throw e;
            }
            
            // 2. Descuenta el stock en el Inventario
            try {
                inventarioClient.reducirStock(detalle.getSkuProducto(), detalle.getCantidad());
                log.debug("Stock reducido para SKU: {}, Cantidad: {}", 
                         detalle.getSkuProducto(), detalle.getCantidad());
            } catch (Exception e) {
                log.error("Error al reducir stock para SKU {}: {}", 
                         detalle.getSkuProducto(), e.getMessage());
                throw e;
            }
        });
 
        Venta nuevaVenta = Venta.builder()
                .idCliente(request.getIdCliente())
                .fechaVenta(LocalDateTime.now())
                .montoTotal(request.getMontoTotal())
                .estado("COMPLETADO")
                .folioBoleta(request.getFolioBoleta())
                .build();
 
        List<DetalleVenta> detalles = request.getDetalles().stream()
                .map(d -> DetalleVenta.builder()
                        .venta(nuevaVenta)
                        .skuProducto(d.getSkuProducto())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .build())
                .collect(Collectors.toList());
 
        nuevaVenta.setDetalles(detalles);
        Venta ventaGuardada = ventaRepository.save(nuevaVenta);
        
        log.info("Venta procesada exitosamente - ID: {}, Folio: {}, Total: ${}", 
                 ventaGuardada.getId(), ventaGuardada.getFolioBoleta(), ventaGuardada.getMontoTotal());
        
        return ventaDTOMapper.toDTO(ventaGuardada);
    }
 
    public VentaResponseDTO obtenerVentaPorFolio(String folioBoleta) {
        log.info("Buscando venta con folio: {}", folioBoleta);
        Venta venta = ventaRepository.findByFolioBoleta(folioBoleta);
 
        if (venta == null) {
            log.warn("Venta no encontrada con folio: {}", folioBoleta);
            throw new RecursoNoEncontradoException("Venta no encontrada con el número de folio: " + folioBoleta);
        }
 
        log.info("Venta encontrada - Folio: {}, Estado: {}", folioBoleta, venta.getEstado());
        return ventaDTOMapper.toDTO(venta);
    }
 
    public VentaResponseDTO actualizarVenta(VentaRequestDTO request) {
        log.info("Actualizando venta con folio: {}", request.getFolioBoleta());
        Venta ventaExistente = ventaRepository.findByFolioBoleta(request.getFolioBoleta());
        
        if (ventaExistente == null) {
            log.warn("Venta no encontrada para actualizar - Folio: {}", request.getFolioBoleta());
            throw new RecursoNoEncontradoException("Número de folio incorrecto o venta no encontrada.");
        }
 
        Venta ventaActualizada = Venta.builder()
                .id(ventaExistente.getId())
                .idCliente(request.getIdCliente())
                .fechaVenta(ventaExistente.getFechaVenta())
                .montoTotal(request.getMontoTotal())
                .estado(ventaExistente.getEstado())
                .folioBoleta(request.getFolioBoleta())
                .build();
 
        List<DetalleVenta> nuevosDetalles = request.getDetalles().stream()
                .map(d -> DetalleVenta.builder()
                        .venta(ventaActualizada)
                        .skuProducto(d.getSkuProducto())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .build())
                .collect(Collectors.toList());
 
        ventaActualizada.setDetalles(nuevosDetalles);
        Venta guardada = ventaRepository.save(ventaActualizada);
        
        log.info("Venta actualizada exitosamente - Folio: {}", request.getFolioBoleta());
        return ventaDTOMapper.toDTO(guardada);
    }
 
    public VentaResponseDTO actualizarEstadoVenta(String folioBoleta, String estado) {
        log.info("Actualizando estado de venta - Folio: {}, Nuevo estado: {}", folioBoleta, estado);
        Venta ventaExistente = ventaRepository.findByFolioBoleta(folioBoleta);
        
        if (ventaExistente == null) {
            log.warn("Venta no encontrada para cambiar estado - Folio: {}", folioBoleta);
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }
 
        String estadoAnterior = ventaExistente.getEstado();
        ventaExistente.setEstado(estado);
        Venta actualizada = ventaRepository.save(ventaExistente);
        
        log.info("Estado actualizado - Folio: {}, Estado anterior: {}, Nuevo estado: {}", 
                 folioBoleta, estadoAnterior, estado);
        return ventaDTOMapper.toDTO(actualizada);
    }
 
    public boolean eliminarVenta(String folioBoleta) {
        log.info("Intentando eliminar venta con folio: {}", folioBoleta);
        
        if (!ventaRepository.existsByFolioBoleta(folioBoleta)) {
            log.warn("Venta no encontrada para eliminar - Folio: {}", folioBoleta);
            throw new RecursoNoEncontradoException("Venta no encontrada para eliminar.");
        }
 
        ventaRepository.deleteByFolioBoleta(folioBoleta);
        log.info("Venta eliminada exitosamente - Folio: {}", folioBoleta);
        return true;
    }
}