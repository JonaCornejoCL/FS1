package cl.tipum.blinblineo.ms_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.tipum.blinblineo.ms_inventario.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    
    // método de Spring Data para buscar el stock de una prenda exacta
    Inventario findBySku(String sku);
}