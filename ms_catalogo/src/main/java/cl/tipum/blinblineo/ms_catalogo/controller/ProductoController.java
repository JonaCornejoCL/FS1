package cl.tipum.blinblineo.ms_catalogo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.tipum.blinblineo.ms_catalogo.dto.ProductoDTO;
import cl.tipum.blinblineo.ms_catalogo.dto.ProductoDTOMapper;
import cl.tipum.blinblineo.ms_catalogo.dto.ProductoRequestDTO;
import cl.tipum.blinblineo.ms_catalogo.model.Producto;
import cl.tipum.blinblineo.ms_catalogo.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Tag(name = "Catálogo de Productos", description = "Endpoints para la gestión del catálogo de Blinblineo")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoDTOMapper dtoMapper;

    @Operation(summary = "Listar todos los productos", description = "Obtiene una lista completa de los productos disponibles en el catálogo")
    @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente")
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        List<ProductoDTO> lista = productoService.listar().stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener producto por SKU", description = "Busca y retorna un producto específico utilizando su código SKU")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{sku}")
    public ResponseEntity<ProductoDTO> obtenerPorSku(@PathVariable String sku) {
        // Se cambió obtenerPorId a obtenerPorSku para mantener coherencia semántica si tu llave primaria es el SKU
        Producto producto = productoService.obtenerPorSku(sku); 
        return ResponseEntity.ok(dtoMapper.toDTO(producto));
    }

    @Operation(summary = "Crear un nuevo producto", description = "Registra un nuevo producto en el catálogo validando sus datos de entrada")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        Producto producto = dtoMapper.toEntity(dto);
        Producto creado = productoService.crear(producto);
        
        // Mejor práctica: usar HttpStatus en lugar de números mágicos (201)
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toDTO(creado));
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente mediante su SKU")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{sku}")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable String sku,
            @Valid @RequestBody ProductoRequestDTO dto) {

        Producto producto = dtoMapper.toEntity(dto, sku);
        Producto actualizado = productoService.actualizar(sku, producto);

        return ResponseEntity.ok(dtoMapper.toDTO(actualizado));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina físicamente o inactiva un producto del catálogo utilizando su SKU")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> eliminar(@PathVariable String sku) {
        productoService.eliminar(sku);
        return ResponseEntity.noContent().build();
    }
}