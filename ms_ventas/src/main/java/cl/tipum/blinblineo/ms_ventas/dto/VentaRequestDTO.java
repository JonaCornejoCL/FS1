package cl.tipum.blinblineo.ms_ventas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VentaRequestDTO {
// Validaciones básicas para asegurar que los datos recibidos sean correctos antes de procesar la venta
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;
// El monto total debe ser positivo y no nulo para evitar ventas con montos inválidos
    @NotNull(message = "El monto total es obligatorio")
    @Min(value = 1, message = "El monto total debe ser mayor a cero")
    private Integer montoTotal;
// El folio de la boleta es un identificador importante para la venta, por lo que no puede estar vacío
    @NotBlank(message = "El folio de la boleta no puede estar vacío")
    private String folioBoleta;

    // @Valid asegura que cada detalle dentro de la lista también se valide según las reglas definidas en DetalleVentaRequestDTO
    @NotEmpty(message = "La venta debe tener al menos un producto")
    @Valid
    private List<DetalleVentaRequestDTO> detalles;
}