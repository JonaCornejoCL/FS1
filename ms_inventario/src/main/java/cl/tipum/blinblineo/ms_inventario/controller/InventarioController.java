package cl.tipum.blinblineo.ms_inventario.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.tipum.blinblineo.ms_inventario.dto.InventarioDTO;
import cl.tipum.blinblineo.ms_inventario.model.Inventario;
import cl.tipum.blinblineo.ms_inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    // GET: Ver todo el stock
    @GetMapping
    public ResponseEntity<List<InventarioDTO>> obtenerTodos() {
        List<InventarioDTO> lista = inventarioService.obtenerTodos().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // GET: Ver stock de un producto
    @GetMapping("/{sku}")
    public ResponseEntity<InventarioDTO> obtenerPorSku(@PathVariable String sku) {
        return ResponseEntity.ok(convertirADto(inventarioService.obtenerPorSku(sku)));
    }

    // PUT: Descontar stock (Usamos PUT porque estamos actualizando un dato)
    // Ejemplo de uso: PUT http://localhost:8082/api/v1/inventario/POL-OVER-BLK/reducir?cantidad=2
    @PutMapping("/{sku}/reducir")
    public ResponseEntity<InventarioDTO> reducirStock(@PathVariable String sku, @RequestParam Integer cantidad) {
        Inventario actualizado = inventarioService.reducirStock(sku, cantidad);
        return ResponseEntity.ok(convertirADto(actualizado));
    }

    private InventarioDTO convertirADto(Inventario inventario) {
        return InventarioDTO.builder()
                .sku(inventario.getSku())
                .cantidad(inventario.getCantidad())
                .build();
    }
}