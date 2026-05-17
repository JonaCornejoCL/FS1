package cl.tipum.blinblineo.ms_ventas.repository;

import cl.tipum.blinblineo.ms_ventas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}