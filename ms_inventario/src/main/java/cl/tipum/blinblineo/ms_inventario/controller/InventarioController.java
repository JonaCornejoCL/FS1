package cl.tipum.blinblineo.ms_inventario.controller;
 
import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import cl.tipum.blinblineo.ms_inventario.dto.InventarioDTOMapper;
import cl.tipum.blinblineo.ms_inventario.dto.InventarioDTO;
import cl.tipum.blinblineo.ms_inventario.dto.InventarioRequestDTO;
import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
 
@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
public class InventarioController {
 
    private final InventarioService inventarioService;
    private final InventarioDTOMapper dtoMapper;
 
    // GET: Ver todo el stock
    @GetMapping
    public ResponseEntity<List<InventarioDTO>> obtenerTodos() {
        List<InventarioDTO> lista = inventarioService.obtenerTodos().stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
 
    // GET: Ver stock de un producto
    @GetMapping("/{sku}")
    public ResponseEntity<InventarioDTO> obtenerPorSku(@PathVariable String sku) {
        Inventario inventario = inventarioService.obtenerPorSku(sku);
        return ResponseEntity.ok(dtoMapper.toDTO(inventario));
    }
 
    // POST: Inicializar stock de una prenda nueva
    @PostMapping
    public ResponseEntity<InventarioDTO> inicializarStock(@Valid @RequestBody InventarioRequestDTO requestDTO) {
        Inventario inventario = dtoMapper.toEntity(
            InventarioDTO.builder()
                .sku(requestDTO.getSku())
                .cantidad(requestDTO.getCantidad())
                .build()
        );
        
        Inventario nuevo = inventarioService.inicializarStock(inventario);
        return new ResponseEntity<>(dtoMapper.toDTO(nuevo), HttpStatus.CREATED);
    }
 
    // PUT: Descontar stock
    @PutMapping("/{sku}/reducir")
    public ResponseEntity<InventarioDTO> reducirStock(
            @PathVariable String sku, 
            @RequestParam Integer cantidad) {
        Inventario actualizado = inventarioService.reducirStock(sku, cantidad);
        return ResponseEntity.ok(dtoMapper.toDTO(actualizado));
    }
}