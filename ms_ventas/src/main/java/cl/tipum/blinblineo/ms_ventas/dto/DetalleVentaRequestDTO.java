package cl.tipum.blinblineo.ms_ventas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleVentaRequestDTO {

    @NotBlank(message = "El SKU del producto es obligatorio")
    private String skuProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 1, message = "El precio unitario no puede ser cero o negativo")
    private Integer precioUnitario;
}