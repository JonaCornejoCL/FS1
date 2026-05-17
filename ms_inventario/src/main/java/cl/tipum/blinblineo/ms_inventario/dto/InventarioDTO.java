package cl.tipum.blinblineo.ms_inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {

    private String sku;
    private Integer cantidad;

}