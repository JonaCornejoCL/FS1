package cl.tipum.blinblineo.ms_ventas.repository;

import cl.tipum.blinblineo.ms_ventas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}