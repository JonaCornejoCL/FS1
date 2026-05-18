package cl.tipum.blinblineo.ms_catalogo.dto;
 
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * DTO para solicitudes de creación/actualización de productos
 * Incluye validaciones Bean Validation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {
 
    @NotBlank(message = "El SKU no puede estar vacío")
    @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
    private String sku;
 
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
 
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
 
    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Integer precio;
 
    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;
}