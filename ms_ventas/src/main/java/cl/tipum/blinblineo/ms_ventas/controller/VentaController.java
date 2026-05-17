package cl.tipum.blinblineo.ms_ventas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.tipum.blinblineo.ms_ventas.dto.VentaRequestDTO;
import cl.tipum.blinblineo.ms_ventas.dto.VentaResponseDTO;
import cl.tipum.blinblineo.ms_ventas.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ventas") // Versión estilo profe
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

    @GetMapping("/{folio}")
    public ResponseEntity<VentaResponseDTO> obtenerBoletaPorFolio(@PathVariable String folio) {
        return ResponseEntity.ok(ventaService.obtenerVentaPorFolio(folio));
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> registrarVenta(@Valid @RequestBody VentaRequestDTO request) {
        return new ResponseEntity<>(ventaService.procesarVenta(request), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<VentaResponseDTO> actualizaVenta(@Valid @RequestBody VentaRequestDTO request) {
        return ResponseEntity.ok(ventaService.actualizarVenta(request));
    }

    @PutMapping("/estados")
    public ResponseEntity<VentaResponseDTO> actualizaEstadoVenta(@RequestParam String folio, @RequestParam String estado) {
        return ResponseEntity.ok(ventaService.actualizarEstadoVenta(folio, estado));
    }

    @DeleteMapping("/{folio}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable String folio) {
        ventaService.eliminarVenta(folio);
        return ResponseEntity.noContent().build();
    }
}