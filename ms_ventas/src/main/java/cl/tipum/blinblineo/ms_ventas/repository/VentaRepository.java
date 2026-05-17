package cl.tipum.blinblineo.ms_ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.tipum.blinblineo.ms_ventas.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    // Método  para validar duplicados
    boolean existsByFolioBoleta(String folioBoleta);
    
    // Método para búsquedas individuales
    Venta findByFolioBoleta(String folioBoleta);

    // Método para eliminar por folio
    void deleteByFolioBoleta(String folioBoleta);
}