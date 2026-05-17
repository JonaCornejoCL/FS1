package cl.tipum.blinblineo.ms_catalogo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.tipum.blinblineo.ms_catalogo.dto.ProductoDTO;
import cl.tipum.blinblineo.ms_catalogo.model.Producto;
import cl.tipum.blinblineo.ms_catalogo.service.ProductoService;
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
        
        // convierte la lista de Entidades a una lista de DTOs
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

    // método auxiliar privado para no repetir la lógica de conversión
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
