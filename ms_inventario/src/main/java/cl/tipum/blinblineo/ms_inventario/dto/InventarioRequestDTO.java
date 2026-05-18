package cl.tipum.blinblineo.ms_inventario.dto;
 
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * DTO para solicitudes de creación/actualización de inventario
 * Incluye validaciones Bean Validation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioRequestDTO {
 
    @NotBlank(message = "El SKU no puede estar vacío")
    @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
    private String sku;
 
    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
}
 