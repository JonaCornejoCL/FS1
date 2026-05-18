package cl.tipum.blinblineo.ms_ventas.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cl.tipum.blinblineo.ms_ventas.model.Venta;

@Component
public class VentaDTOMapper {

    public VentaResponseDTO toDTO(Venta venta) {
        if (venta == null) {
            return null;
        }

        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setFolioBoleta(venta.getFolioBoleta()); 
        dto.setIdCliente(venta.getIdCliente()); 
        dto.setMontoTotal(venta.getMontoTotal());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setEstado(venta.getEstado());

        if (venta.getDetalles() != null) {
            List<DetalleVentaResponseDTO> detallesDTO = venta.getDetalles().stream().map(d -> {
                DetalleVentaResponseDTO detDto = new DetalleVentaResponseDTO();
                detDto.setSkuProducto(d.getSkuProducto());
                detDto.setCantidad(d.getCantidad());
                detDto.setPrecioUnitario(d.getPrecioUnitario());
                return detDto;
            }).collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }

        return dto;
    }
}