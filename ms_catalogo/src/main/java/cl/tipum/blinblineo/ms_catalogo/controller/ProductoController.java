package cl.tipum.blinblineo.ms_catalogo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.tipum.blinblineo.ms_catalogo.dto.ProductoDTO;
import cl.tipum.blinblineo.ms_catalogo.model.Producto;
import cl.tipum.blinblineo.ms_catalogo.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // 1. Endpoint para ver toda la vitrina
    // GET http://localhost:8081/api/v1/productos
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        
        List<ProductoDTO> productosDTO = productos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(productosDTO);
    }

    // 2. Endpoint para buscar una prenda exacta por su SKU
    // GET http://localhost:8081/api/v1/productos/{sku}
    @GetMapping("/{sku}")
    public ResponseEntity<ProductoDTO> obtenerPorSku(@PathVariable String sku) {
        Producto producto = productoService.obtenerPorSku(sku);
        return ResponseEntity.ok(convertirADto(producto));
    }

    // 3. Endpoint para registrar un nuevo producto (¡El que faltaba para la prueba!)
    // POST http://localhost:8081/api/v1/productos
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody Producto producto) {
        // NOTA: Revisa si en tu ProductoService el método se llama 'guardar' o 'crear'
        Producto nuevoProducto = productoService.guardar(producto); 
        return new ResponseEntity<>(convertirADto(nuevoProducto), HttpStatus.CREATED);
    }

    // 4. Endpoint para actualizar stock, precios o datos de una prenda
    // PUT http://localhost:8081/api/v1/productos/{sku}
    @PutMapping("/{sku}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable String sku, @Valid @RequestBody Producto producto) {
        // NOTA: Revisa si en tu ProductoService el método se llama 'actualizar'
        Producto productoActualizado = productoService.actualizar(sku, producto);
        return ResponseEntity.ok(convertirADto(productoActualizado));
    }

    // 5. Endpoint para eliminar una prenda de la vitrina
    // DELETE http://localhost:8081/api/v1/productos/{sku}
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String sku) {
        // NOTA: Revisa si en tu ProductoService el método se llama 'eliminar' o 'borrar'
        productoService.eliminar(sku);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar privado para la conversión de Entidad a DTO
    private ProductoDTO convertirADto(Producto producto) {
        return ProductoDTO.builder()
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .estado(producto.getEstado())
                .build();
    }
}