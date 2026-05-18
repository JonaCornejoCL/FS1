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
import cl.tipum.blinblineo.ms_catalogo.dto.ProductoDTOMapper;
import cl.tipum.blinblineo.ms_catalogo.dto.ProductoRequestDTO;
import cl.tipum.blinblineo.ms_catalogo.model.Producto;
import cl.tipum.blinblineo.ms_catalogo.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
 
/**
 * CORRECCIONES APLICADAS:
 * - Uso de DTOMapper dedicado como @Component
 * - @Valid en POST y PUT con DTO de entrada que tiene Bean Validation
 * - Controller delega 100% al Service
 */
@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {
 
    private final ProductoService productoService;
    private final ProductoDTOMapper dtoMapper;
 
    // 1. Endpoint para ver toda la vitrina
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        List<ProductoDTO> productosDTO = productoService.obtenerTodos().stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(productosDTO);
    }
 
    // 2. Endpoint para buscar una prenda exacta por su SKU
    @GetMapping("/{sku}")
    public ResponseEntity<ProductoDTO> obtenerPorSku(@PathVariable String sku) {
        Producto producto = productoService.obtenerPorSku(sku);
        return ResponseEntity.ok(dtoMapper.toDTO(producto));
    }
 
    // 3. Endpoint para registrar un nuevo producto - AHORA CON @Valid y DTO
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO requestDTO) {
        Producto producto = dtoMapper.toEntity(ProductoDTO.builder()
                .sku(requestDTO.getSku())
                .nombre(requestDTO.getNombre())
                .descripcion(requestDTO.getDescripcion())
                .precio(requestDTO.getPrecio())
                .estado(requestDTO.getEstado())
                .build());
        
        Producto nuevoProducto = productoService.guardar(producto);
        return new ResponseEntity<>(dtoMapper.toDTO(nuevoProducto), HttpStatus.CREATED);
    }
 
    // 4. Endpoint para actualizar datos de una prenda - AHORA CON @Valid y DTO
    @PutMapping("/{sku}")
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @PathVariable String sku, 
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        
        Producto producto = dtoMapper.toEntity(ProductoDTO.builder()
                .sku(requestDTO.getSku())
                .nombre(requestDTO.getNombre())
                .descripcion(requestDTO.getDescripcion())
                .precio(requestDTO.getPrecio())
                .estado(requestDTO.getEstado())
                .build());
        
        Producto productoActualizado = productoService.actualizar(sku, producto);
        return ResponseEntity.ok(dtoMapper.toDTO(productoActualizado));
    }
 
    // 5. Endpoint para eliminar una prenda de la vitrina
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String sku) {
        productoService.eliminar(sku);
        return ResponseEntity.noContent().build();
    }
}