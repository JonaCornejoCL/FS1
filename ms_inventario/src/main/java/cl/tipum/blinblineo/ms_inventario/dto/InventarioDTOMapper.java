package cl.tipum.blinblineo.ms_inventario.dto;
 
import org.springframework.stereotype.Component;
 
import cl.tipum.blinblineo.ms_inventario.model.Inventario;
 
/**
 * Mapper dedicado para conversiones entre Entidad y DTOs de Inventario.
 */
@Component
public class InventarioDTOMapper {
 
    /**
     * Convierte una entidad Inventario a su representación DTO
     */
    public InventarioDTO toDTO(Inventario inventario) {
        if (inventario == null) {
            return null;
        }
        
        return InventarioDTO.builder()
                .sku(inventario.getSku())
                .cantidad(inventario.getCantidad())
                .build();
    }
 
    /**
     * Convierte un DTO a entidad Inventario
     */
    public Inventario toEntity(InventarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Inventario.builder()
                .sku(dto.getSku())
                .cantidad(dto.getCantidad())
                .build();
    }
}