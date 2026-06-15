package cl.tipum.blinblineo.ms_catalogo.dto;

import org.springframework.stereotype.Component;
import cl.tipum.blinblineo.ms_catalogo.model.Producto;

@Component
public class ProductoDTOMapper {

    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) return null;

        return ProductoDTO.builder()
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .estado(producto.getEstado())
                .build();
    }

    public Producto toEntity(ProductoRequestDTO dto, String sku) {
        if (dto == null) return null;

        return Producto.builder()
                .sku(sku)
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .estado(dto.getEstado())
                .build();
    }

    public Producto toEntity(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setEstado(dto.getEstado());
        
        return producto;
    }
}