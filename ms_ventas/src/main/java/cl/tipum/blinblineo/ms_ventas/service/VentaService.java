package cl.tipum.blinblineo.ms_ventas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.tipum.blinblineo.ms_ventas.dto.VentaDTOMapper;
import cl.tipum.blinblineo.ms_ventas.dto.VentaRequestDTO;
import cl.tipum.blinblineo.ms_ventas.dto.VentaResponseDTO;
import cl.tipum.blinblineo.ms_ventas.exceptions.RecursoNoEncontradoException;
import cl.tipum.blinblineo.ms_ventas.exceptions.RecursoYaExisteException;
import cl.tipum.blinblineo.ms_ventas.model.DetalleVenta;
import cl.tipum.blinblineo.ms_ventas.model.Venta;
import cl.tipum.blinblineo.ms_ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaService {
    // inyección de dependencias
    private final VentaRepository ventaRepository;
    private final VentaDTOMapper ventaDTOMapper; // traductor

    public List<VentaResponseDTO> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaRepository.findAll();
        // validación del profe: para evitar resp vacías
        if (ventas.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron ventas registradas.");
        }

        return ventas.stream()
                .map(ventaDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VentaResponseDTO procesarVenta(VentaRequestDTO request) {
        // validación del profe: para evitar duplicados
        if (ventaRepository.existsByFolioBoleta(request.getFolioBoleta())) {
            throw new RecursoYaExisteException("Ya existe una venta con el folio: " + request.getFolioBoleta());
        }

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
        
        // guarda y devuelve traducido a DTO
        return ventaDTOMapper.toDTO(ventaRepository.save(nuevaVenta));
    }

    public VentaResponseDTO obtenerVentaPorFolio(String folioBoleta) {
        // busqueda por folioBoleta
        cl.tipum.blinblineo.ms_ventas.model.Venta venta = ventaRepository.findByFolioBoleta(folioBoleta);

        // por si no existe
        if (venta == null) {
            throw new RecursoNoEncontradoException("Venta no encontrada con el número de folio: " + folioBoleta);
        }

        // si existe, se devueve convertido a DTO para proteger la BD
        return ventaDTOMapper.toDTO(venta);
    }

    // actualizar la venta completa mapeando un DTO entrante
    public VentaResponseDTO actualizarVenta(VentaRequestDTO request) {
        Venta ventaExistente = ventaRepository.findByFolioBoleta(request.getFolioBoleta());
        
        if (ventaExistente == null || !ventaExistente.getFolioBoleta().equals(request.getFolioBoleta())) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto o venta no encontrada.");
        }

        Venta ventaActualizada = Venta.builder()
                .id(ventaExistente.getId()) // línea clave para que JPA haga un UPDATE y no un INSERT
                .idCliente(request.getIdCliente())
                .fechaVenta(ventaExistente.getFechaVenta())
                .montoTotal(request.getMontoTotal())
                .estado(ventaExistente.getEstado()) // mantiene el estado
                .folioBoleta(request.getFolioBoleta())
                .build();

        // mapea los nuevos detalles de productos
        List<DetalleVenta> nuevosDetalles = request.getDetalles().stream()
                .map(d -> DetalleVenta.builder()
                        .venta(ventaActualizada)
                        .skuProducto(d.getSkuProducto())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .build())
                .collect(Collectors.toList());

        ventaActualizada.setDetalles(nuevosDetalles);

        return ventaDTOMapper.toDTO(ventaRepository.save(ventaActualizada));
    }

    // actualiza únicamente el estado de la venta (Estilo @RequestParam)
    public VentaResponseDTO actualizarEstadoVenta(String folioBoleta, String estado) {
        Venta ventaExistente = ventaRepository.findByFolioBoleta(folioBoleta);
        
        if (ventaExistente == null) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }

        ventaExistente.setEstado(estado);
        return ventaDTOMapper.toDTO(ventaRepository.save(ventaExistente));
    }

    // eliminar una venta por su folio
    public boolean eliminarVenta(String folioBoleta) {
        if (!ventaRepository.existsByFolioBoleta(folioBoleta)) {
            throw new RecursoNoEncontradoException("Venta no encontrada para eliminar.");
        }

        ventaRepository.deleteByFolioBoleta(folioBoleta);
        return true;
    }
}