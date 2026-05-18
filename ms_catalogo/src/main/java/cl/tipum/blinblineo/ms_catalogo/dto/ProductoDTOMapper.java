package cl.tipum.blinblineo.ms_catalogo.dto;
 
import org.springframework.stereotype.Component;
 
import cl.tipum.blinblineo.ms_catalogo.model.Producto;
 
/**
 * Mapper dedicado para conversiones entre Entidad y DTO
 * Patrón más prolijo que la conversión inline en el Controller
 */
@Component
public class ProductoDTOMapper {
 
    /**
     * Convierte una entidad Producto a su representación DTO
     */
    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        return ProductoDTO.builder()
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .estado(producto.getEstado())
                .build();
    }
 
    /**
     * Convierte un DTO a entidad Producto
     */
    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Producto.builder()
                .sku(dto.getSku())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .estado(dto.getEstado())
                .build();
    }
}
 