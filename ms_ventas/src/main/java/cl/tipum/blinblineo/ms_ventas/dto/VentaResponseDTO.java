package cl.tipum.blinblineo.ms_ventas.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class VentaResponseDTO {
    private String folioBoleta;
    private Long idCliente;
    private Integer montoTotal;
    private LocalDateTime fechaVenta;
    private String estado;
    private List<DetalleVentaResponseDTO> detalles;
}