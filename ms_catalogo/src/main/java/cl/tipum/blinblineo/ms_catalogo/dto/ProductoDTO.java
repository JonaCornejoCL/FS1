package cl.tipum.blinblineo.ms_catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private String sku;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String estado;

}