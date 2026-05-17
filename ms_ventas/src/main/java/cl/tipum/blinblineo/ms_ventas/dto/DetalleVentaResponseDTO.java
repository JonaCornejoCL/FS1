package cl.tipum.blinblineo.ms_ventas.dto;

import lombok.Data;

@Data
public class DetalleVentaResponseDTO {
    private String skuProducto;
    private Integer cantidad;
    private Integer precioUnitario;
}